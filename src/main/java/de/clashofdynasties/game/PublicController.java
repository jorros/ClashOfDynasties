package de.clashofdynasties.game;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PublicController
{
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(ModelMap map)
	{
        // ToDo: Check if Login or Game
        return "login";
    }

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String register(ModelMap map)
	{


		return "login";
	}
}
