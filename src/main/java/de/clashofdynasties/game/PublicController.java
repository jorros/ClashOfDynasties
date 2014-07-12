package de.clashofdynasties.game;

import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.NationRepository;
import de.clashofdynasties.repository.PlayerRepository;
import de.clashofdynasties.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublicController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    NationRepository nationRepository;

    @Autowired
    LoginService loginService;

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

    @RequestMapping(value = "/step1", method = RequestMethod.POST)
    public String registerStep1(ModelMap map, @RequestParam String key, @RequestParam String name, @RequestParam String password, @RequestParam String email) {
        Player player = playerRepository.findOne(key);
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        player.setName(name);
        player.setPassword(encoder.encodePassword(password, null));
        player.setEmail(email);
        playerRepository.save(player);

        return "redirect:/register?key=" + key;
    }

    @RequestMapping(value = "/step2", method = RequestMethod.GET)
    public String registerStep2(ModelMap map, @RequestParam String key, @RequestParam int nation) {
        Player player = playerRepository.findOne(key);

        player.setNation(nationRepository.findOne(nation));
        player.setActivated(true);
        playerRepository.save(player);

        UserDetails userDetails = loginService.loadUserByUsername(player.getName());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map, @RequestParam(required = false) String key) {
        if(key != null) {
            Player player = playerRepository.findOne(key);

            if(player != null && !player.isActivated()) {
                map.addAttribute("key", key);

                if(player.getEmail() == null)
                    return "register";
                else
                    return "choosenation";
            }
        }

        return "redirect:/";
    }
}
