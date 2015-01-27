package de.dynastiesofscorpia.game;

import de.dynastiesofscorpia.models.CityType;
import de.dynastiesofscorpia.repository.CityTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/game/citytypes")
@Secured("ROLE_USER")
public class CityTypeController {
    @Autowired
    CityTypeRepository cityTypeRepository;

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.OK)
    public void save(@PathVariable int id, @RequestParam(required = false) Double capacity, @RequestParam(required = false) Double consumeBasic, @RequestParam(required = false) Double consumeLuxury1, @RequestParam(required = false) Double consumeLuxury2, @RequestParam(required = false) Double consumeLuxury3,  @RequestParam(required = false) Double taxes, @RequestParam(required = false) Double productionRate) {
        CityType cityType = cityTypeRepository.findById(id);

        if(capacity != null)
            cityType.setCapacity(capacity);

        if(consumeBasic != null)
            cityType.setConsumeBasic(consumeBasic);

        if(consumeLuxury1 != null)
            cityType.setConsumeLuxury1(consumeLuxury1);

        if(consumeLuxury2 != null)
            cityType.setConsumeLuxury2(consumeLuxury2);

        if(consumeLuxury3 != null)
            cityType.setConsumeLuxury3(consumeLuxury3);

        if(taxes != null)
            cityType.setTaxes(taxes);

        if(productionRate != null)
            cityType.setProductionRate(productionRate);
    }
}
