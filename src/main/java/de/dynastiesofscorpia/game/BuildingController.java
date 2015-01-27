package de.dynastiesofscorpia.game;

import de.dynastiesofscorpia.models.BuildingBlueprint;
import de.dynastiesofscorpia.models.City;
import de.dynastiesofscorpia.repository.BuildingBlueprintRepository;
import de.dynastiesofscorpia.repository.CityRepository;
import de.dynastiesofscorpia.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/game/buildings")
@Secured("ROLE_USER")
public class BuildingController {
    @Autowired
    private BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CityRepository cityRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void save(@PathVariable int id, @RequestParam(required = false) String description, @RequestParam(required = false) Integer production, @RequestParam(required = false) Integer maxcount, @RequestParam(required = false) Integer price, @RequestParam(required = false) Double pps, @RequestParam(required = false) Integer defence, @RequestParam(required = false) Integer item, @RequestParam(required = false) Integer citytype) {
        BuildingBlueprint blueprint = buildingBlueprintRepository.findById(id);

        if (description != null)
            blueprint.setDescription(description);

        if (production != null)
            blueprint.setRequiredProduction(production);

        if(maxcount != null)
            blueprint.setMaxCount(maxcount);

        if (price != null)
            blueprint.setPrice(price);

        if (defence != null) {
            if(defence != blueprint.getDefencePoints()) {
                blueprint.setDefencePoints(defence);

                cityRepository.getList().forEach(City::recalculateStrength);
            }
        }

        if (pps != null)
            blueprint.setProducePerStep(pps);

        if(citytype != null)
            blueprint.setRequiredCityType(citytype);

        if(item != null) {
            if(item > 0)
                blueprint.setProduceItem(itemRepository.findById(item));
            else
                blueprint.setProduceItem(null);
        }
    }

    @RequestMapping(value = "/{building}/icon", headers = "Accept=image/png", method = RequestMethod.GET)
    public ResponseEntity health(HttpServletRequest request, @PathVariable int building, @RequestParam double health) {
        try {
            InputStream is = request.getSession().getServletContext().getResourceAsStream("/resources/assets/buildings/" + building + ".png");
            BufferedImage img = ImageIO.read(is);

            int width = img.getWidth();
            int height = img.getHeight();

            int healthWidth = (int) (health / (double) 100 * width);

            for (int xx = 0; xx < width; xx++) {
                for (int yy = 0; yy < height; yy++) {
                    Color originalColor = new Color(img.getRGB(xx, yy), true);

                    Color red = new Color(Color.RED.getRed(), Color.RED.getGreen(), Color.RED.getBlue(), originalColor.getAlpha());
                    img.setRGB(xx, yy, red.getRGB());
                }
            }

            for (int xx = 0; xx < healthWidth; xx++) {
                for (int yy = 0; yy < height; yy++) {
                    Color originalColor = new Color(img.getRGB(xx, yy), true);

                    Color green = new Color(Color.GREEN.getRed(), Color.GREEN.getGreen(), Color.GREEN.getBlue(), originalColor.getAlpha());
                    img.setRGB(xx, yy, green.getRGB());
                }
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img, "png", baos);
            byte[] imageInByte = baos.toByteArray();

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("image", "png"));
            return new ResponseEntity(imageInByte, headers, HttpStatus.CREATED);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
