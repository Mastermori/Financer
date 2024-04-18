package de.moritzjunge.financer.services;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Role;
import de.moritzjunge.financer.model.dtos.UserDTO;
import de.moritzjunge.financer.repositories.UserRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        register(new UserDTO().setName("admin").setPassword("admin"));
        register(new UserDTO().setName("test1").setPassword("test1"));
        register(new UserDTO().setName("test2").setPassword("test2"));
    }

    public void register(UserDTO newUser) {
        FUser user = new FUser();
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPasswordHash(passwordEncoder.encode(newUser.getPassword()));
        user.setRole(Role.USER);
        addUser(user);
        System.out.println("Added user");
    }

    public List<FUser> getUsers() {
        return userRepository.findAll();
    }

    public Optional<FUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public FUser findUserByName(String name) {
        return userRepository.findByName(name);
    }

    private void addUser(FUser user) {
        userRepository.save(user);
    }

    @Nullable
    public FUser getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        String username = authentication.getName();
        return findUserByName(username);
    }

}
