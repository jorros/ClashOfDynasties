package de.clashofdynasties.game;

import de.clashofdynasties.models.BuildingBlueprint;
import de.clashofdynasties.repository.BuildingBlueprintRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game/buildings")
public class BuildingController
{
    @Autowired
    BuildingBlueprintRepository buildingBlueprintRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void save(@PathVariable int id, @RequestParam(required = false) String description, @RequestParam(required = false) Integer production, @RequestParam(required = false) Integer price)
    {
        BuildingBlueprint blueprint = buildingBlueprintRepository.findOne(id);

        if(description != null)
            blueprint.setDescription(description);

        if(production != null)
            blueprint.setRequiredProduction(production);

        if(price != null)
            blueprint.setPrice(price);

        buildingBlueprintRepository.save(blueprint);
    }
}
