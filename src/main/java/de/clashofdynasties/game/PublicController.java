package de.clashofdynasties.game;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicController {
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map) {
        if (request.isUserInRole("ROLE_USER"))
            return "game";
        else
            return "login";
    }

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String editor() {
        return "editor";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map) {


        return "login";
    }
}
