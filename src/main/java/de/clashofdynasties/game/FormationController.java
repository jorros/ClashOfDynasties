package de.clashofdynasties.game;

import de.clashofdynasties.models.City;
import de.clashofdynasties.models.Formation;
import de.clashofdynasties.models.Player;
import de.clashofdynasties.models.Unit;
import de.clashofdynasties.repository.CityRepository;
import de.clashofdynasties.repository.FormationRepository;
import de.clashofdynasties.repository.PlayerRepository;
import de.clashofdynasties.repository.UnitRepository;
import de.clashofdynasties.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FormationController
{
    @Autowired
    FormationRepository formationRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    UnitRepository unitRepository;

    @Autowired
    CounterService counterService;

    @RequestMapping(value = "/game/formations/all", method = RequestMethod.GET)
    public @ResponseBody
    Map<Integer, Formation> loadAllFormations(Principal principal)
    {
        Player player = playerRepository.findByName(principal.getName());

        List<Formation> formations = formationRepository.findAll();
        HashMap<Integer, Formation> data = new HashMap<Integer, Formation>();

        for(Formation formation : formations)
        {
            if(formation.getRoute() != null && !formation.getRoute().isEmpty())
                data.put(formation.getId(), formation);
        }

        return data;
    }

    @RequestMapping(value="/game/menu/formation", method = RequestMethod.GET)
    public String showSetupMenu(ModelMap map, Principal principal, @RequestParam(value = "formation", required = false) Integer id, @RequestParam(value = "city", required = false) Integer cityID)
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

    @RequestMapping(value="/game/formations/health", headers = "Accept=image/png", method = RequestMethod.GET)
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

    @RequestMapping(value="/game/controls/formation", method = RequestMethod.GET)
    public String showInfoMenu(ModelMap map, Principal principal, @RequestParam("formation") int id)
    {
        Formation formation = formationRepository.findOne(id);

        map.addAttribute("player", playerRepository.findByName(principal.getName()));
        map.addAttribute("formation", formation);

        return "formation/info";
    }

    @RequestMapping(value="/game/formations/save", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String save(Principal principal, @RequestParam("formation") int formationId, @RequestParam("name") String name, @RequestParam("city") int cityId, @RequestParam("units[]") List<Integer> unitsId)
    {
        // Formation, City und Player vorbereiten
        City city = cityRepository.findOne(cityId);
        Player player = playerRepository.findByName(principal.getName());

        // Formation nur fetchen, wenn City dem Spieler gehört
        if(player.equals(city.getPlayer()))
        {
            Formation formation;
            if(formationId > 0)
                formation = formationRepository.findOne(formationId);
            else
            {
                formation = new Formation();
                formation.setId(counterService.getNextSequence("formation"));
                formation.setUnits(new ArrayList<Unit>());
                formation.setHealth(100);
                formation.setLastCity(city);
                formation.setRoute(new ArrayList<City>());
                formation.setPlayer(player);
            }

            if(player.equals(formation.getPlayer()))
            {
                if(formation.getRoute().isEmpty())
                {
                    List<Unit> units = new ArrayList<Unit>();
                    for(int unitId : unitsId)
                    {
                        Unit unit = unitRepository.findOne(unitId);
                        if(city.getUnits().contains(unit))
                        {
                            city.getUnits().remove(unit);
                            formation.getUnits().add(unit);
                        }
                    }

                    formation.setName(name);

                    cityRepository.save(city);
                    formationRepository.save(formation);
                }
            }
        }

        return null;
    }
}
