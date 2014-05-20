package de.clashofdynasties.service;

import de.clashofdynasties.models.Player;
import de.clashofdynasties.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;

public class CustomUserDetailsService implements UserDetailsService {
    private MongoTemplate mongoTemplate;

    @Autowired
    PlayerRepository playerRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Player player = getUserDetail(username);

        if (player == null)
            System.out.println("Fehler");

        ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (player.getName().equalsIgnoreCase("jorros"))
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        User userDetail = new User(player.getName(), player.getPassword(), true, true, true, true, authorities);
        return userDetail;
    }

    @Autowired
    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Player getUserDetail(String username) {
        //MongoOperations mongoOperation = (MongoOperations)mongoTemplate;
        //Player player = mongoOperation.findOne(new Query(Criteria.where("name").is(username)), Player.class);
        Player player = playerRepository.findByNameIgnoreCase(username);
        return player;
    }
}
