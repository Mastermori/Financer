package de.moritzjunge.financer.config;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        FUser user = userRepository.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("Unknown user " + username);
        }
        return User.withUsername(username).password(user.getPasswordHash()).authorities(user.getRole().getAuthority()).build();
    }

}
