package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.dtos.HouseholdDTO;
import de.moritzjunge.financer.services.UserService;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserControllerAdvice {

    private final UserService userService;

    public UserControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute(name = "currentUser", binding = false)
    public FUser getUser(){
        return userService.getAuthenticatedUser();
    }

    @ModelAttribute(name = "newHousehold", binding = true)
    public HouseholdDTO getNewHousehold() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return HouseholdDTO.fromEntities(new Household().setOwner(userService.getAuthenticatedUser()).setName(""));
    }

    @ModelAttribute(name = "otherUsers", binding = false)
    public Set<FUser> getOtherUsers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return new HashSet<>();
        }
        List<FUser> users = userService.getUsers();
        Long currentUserId = userService.getAuthenticatedUser().getId();
        return users.stream().filter(user -> !user.getId().equals(currentUserId)).collect(Collectors.toSet());
    }

}
