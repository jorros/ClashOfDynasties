package de.dynastiesofscorpia.game;

import de.dynastiesofscorpia.models.Player;
import de.dynastiesofscorpia.repository.NationRepository;
import de.dynastiesofscorpia.repository.PlayerRepository;
import de.dynastiesofscorpia.service.LoginService;
import org.bson.types.ObjectId;
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
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PublicController {
    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    NationRepository nationRepository;

    @Autowired
    LoginService loginService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(HttpServletRequest request, ModelMap map, Principal principal) {
        if (request.isUserInRole("ROLE_USER")) {
            if(playerRepository.getList().stream().filter(Player::hasWon).count() > 0) {
                Player player = playerRepository.findByName(principal.getName());
                List<Player> players = playerRepository.getList().stream().filter(p -> p.getStatistic() != null).collect(Collectors.toList());

                Collections.sort(players, (Player p1, Player p2) -> Integer.compare(p1.getStatistic().getRank(), p2.getStatistic().getRank()));

                Player maxDemography = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getDemography(), p2.getStatistic().getDemography())).get();
                Player maxEconomy = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getEconomy(), p2.getStatistic().getEconomy())).get();
                Player maxMilitary = players.stream().max((Player p1, Player p2) -> Integer.compare(p1.getStatistic().getMilitary(), p2.getStatistic().getMilitary())).get();

                map.addAttribute("player", player);
                map.addAttribute("players", players);
                map.addAttribute("maxDemography", maxDemography);
                map.addAttribute("maxEconomy", maxEconomy);
                map.addAttribute("maxMilitary", maxMilitary);
                map.addAttribute("winner", playerRepository.getList().stream().filter(Player::hasWon).findAny().get());

                return "winner";
            }
            else
                return "game";
        }
        else
            return "login";
    }

    @RequestMapping(value = "/editor", method = RequestMethod.GET)
    @Secured("ROLE_ADMIN")
    public String editor() {
        return "editor";
    }

    @RequestMapping(value = "/step1", method = RequestMethod.POST)
    public String registerStep1(ModelMap map, @RequestParam ObjectId key, @RequestParam String name, @RequestParam String password, @RequestParam String email) {
        Player player = playerRepository.findById(key);
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();

        if(playerRepository.findByNameIgnoreCase(name) != null)
            return "redirect:/register?key=" + key + "&error=true";

        player.setName(name);
        player.setPassword(encoder.encodePassword(password, null));
        player.setEmail(email);

        return "redirect:/register?key=" + key;
    }

    @RequestMapping(value = "/step2", method = RequestMethod.GET)
    public String registerStep2(ModelMap map, @RequestParam ObjectId key, @RequestParam int nation) {
        Player player = playerRepository.findById(key);

        player.setNation(nationRepository.findById(nation));

        return "redirect:/register?key=" + key;
    }

    @RequestMapping(value = "/step3", method = RequestMethod.GET)
    public String registerStep3(ModelMap map, @RequestParam ObjectId key, @RequestParam int color) {
        Player player = playerRepository.findById(key);

        if(playerRepository.getList().stream().filter(p -> p.getColor() == color).count() > 0)
            return "redirect:/register?key=" + key + "&error=true";

        player.setColor(color);
        player.setActivated(true);

        UserDetails userDetails = loginService.loadUserByUsername(player.getName());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        return "redirect:/";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(ModelMap map, @RequestParam(required = false) ObjectId key, @RequestParam(required = false) Boolean error) {
        if(key != null) {
            Player player = playerRepository.findById(key);

            if(player != null && !player.isActivated()) {
                map.addAttribute("key", key);

                if(error != null)
                    map.addAttribute("error", error);

                if(player.getEmail() == null)
                    return "register";
                else if(player.getNation() == null)
                    return "choosenation";
                else {
                    List<Integer> notAvailableColors = playerRepository.getList().stream().map(Player::getColor).distinct().collect(Collectors.toList());
                    map.addAttribute("notAvailableColors", notAvailableColors);

                    return "choosecolor";
                }
            }
        }

        return "redirect:/";
    }
}
