package de.clashofdynasties.game;

import de.clashofdynasties.models.BuildingBlueprint;
import de.clashofdynasties.repository.BuildingBlueprintRepository;
import de.clashofdynasties.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game/buildings")
public class BuildingController {
    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @Autowired
    ItemRepository itemRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void save(@PathVariable int id, @RequestParam(required = false) String description, @RequestParam(required = false) Integer production, @RequestParam(required = false) Integer maxcount, @RequestParam(required = false) Integer price, @RequestParam(required = false) Double pps, @RequestParam(required = false) Integer defence, @RequestParam(required = false) Integer item) {
        BuildingBlueprint blueprint = buildingBlueprintRepository.findOne(id);

        if (description != null)
            blueprint.setDescription(description);

        if (production != null)
            blueprint.setRequiredProduction(production);

        if(maxcount != null)
            blueprint.setMaxCount(maxcount);

        if (price != null)
            blueprint.setPrice(price);

        if (defence != null)
            blueprint.setDefencePoints(defence);

        if (pps != null)
            blueprint.setProducePerStep(pps);

        if(item != null) {
            if(item > 0)
                blueprint.setProduceItem(itemRepository.findOne(item));
            else
                blueprint.setProduceItem(null);
        }

        buildingBlueprintRepository.save(blueprint);
    }
}
