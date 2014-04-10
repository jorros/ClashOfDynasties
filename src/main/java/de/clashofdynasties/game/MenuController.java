package de.clashofdynasties.game;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/game/menus")
public class MenuController
{
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    FormationRepository formationRepository;

    @Autowired
    UnitBlueprintRepository unitBlueprintRepository;

    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/top", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> getTop(Principal principal)
    {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Player player = playerRepository.findByName(principal.getName());
        List<City> cities = cityRepository.findByPlayer(player);

        int people = 0;
        int balance = 0;

        for(City city : cities)
        {
            people += city.getPopulation();
            balance += city.getIncome() - city.getOutcome();
        }

        map.put("player", player);
        map.put("cities", cities);
        map.put("people", people);
        map.put("balance", balance);
        map.put("cityNum", cities.size());

        return map;
    }

    @RequestMapping(value="/formation", method = RequestMethod.GET)
    public String showFormationSetup(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) Integer id, @RequestParam(value = "city", required = false) Integer cityID)
    {
        if(id != null)
        {
            Formation formation = formationRepository.findOne(id);
            map.addAttribute("formation", formation);
            map.addAttribute("city", formation.getLastCity());
        }
        else if(cityID != null)
        {
            Formation formation = new Formation();
            formation.setName("Neue Formation");

            map.addAttribute("formation", formation);
            map.addAttribute("city", cityRepository.findOne(cityID));
        }

        return "formation/setup";
    }

    @RequestMapping(value="/unit", headers = "Accept=image/png", method = RequestMethod.GET)
    public ResponseEntity getUnitHealth(HttpServletRequest request, @RequestParam("unit") int unit, @RequestParam("health") double health)
    {
        try
        {
            InputStream is = request.getSession().getServletContext().getResourceAsStream("/resources/assets/units/" + unit + ".png");
            BufferedImage img = ImageIO.read(is);

            int width = img.getWidth();
            int height = img.getHeight();

            int healthWidth = (int)(health / (double)100 * width);

            for(int xx = 0; xx < width; xx++)
            {
                for(int yy = 0; yy < height; yy++)
                {
                    Color originalColor = new Color(img.getRGB(xx, yy), true);

                    Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), originalColor.getAlpha());
                    img.setRGB(xx, yy, red.getRGB());
                }
            }

            for(int xx = 0; xx < healthWidth; xx++)
            {
                for(int yy = 0; yy < height; yy++)
                {
                    Color originalColor = new Color(img.getRGB(xx, yy), true);

                    Color green = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), originalColor.getAlpha());
                    img.setRGB(xx, yy, green.getRGB());
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] imageInByte = baos.toByteArray();

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("image","png"));
            return new ResponseEntity(imageInByte, headers, HttpStatus.CREATED);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value="/build", method = RequestMethod.GET)
    public String showBuild(ModelMap map, Principal principal, @RequestParam("city") int id)
    {
        map.addAttribute("city", cityRepository.findOne(id));
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll());
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll());
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "city/build";
    }

    @RequestMapping(value="/store", method = RequestMethod.GET)
    public String showStore(ModelMap map, Principal principal, @RequestParam("city") int id) {
        City city = cityRepository.findOne(id);

        map.addAttribute("city", city);
        map.addAttribute("items", itemRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));
        map.addAttribute("player", playerRepository.findByName(principal.getName()));

        return "city/items";
    }

	@RequestMapping(value = "/ranking", method = RequestMethod.GET)
	public String showRanking(ModelMap map)
	{
		return "ranking";
	}

	@RequestMapping(value = "/settings", method = RequestMethod.GET)
	public String showSettings(ModelMap map)
	{
		return "settings";
	}

	@RequestMapping(value = "/demography", method = RequestMethod.GET)
	public String showDemography(ModelMap map)
	{
		return "demography";
	}

	@RequestMapping(value = "/diplomacy", method = RequestMethod.GET)
	public String showDiplomacy(ModelMap map)
	{
		return "diplomacy";
	}

    @RequestMapping(value="/editresources", method = RequestMethod.GET)
    public String showEditResources(ModelMap map)
    {
        map.addAttribute("items", itemRepository.findAll());

        return "editor/building";
    }

    @RequestMapping(value="/editbuildings", method = RequestMethod.GET)
    public String showEditBuildings(ModelMap map)
    {
        map.addAttribute("buildingBlueprints", buildingBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "editor/building";
    }

    @RequestMapping(value="/editunits", method = RequestMethod.GET)
    public String showEditUnits(ModelMap map)
    {
        map.addAttribute("unitBlueprints", unitBlueprintRepository.findAll(new Sort(Sort.Direction.ASC, "_id")));

        return "editor/unit";
    }
}
