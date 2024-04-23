package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.dtos.HouseholdDTO;
import de.moritzjunge.financer.services.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @ModelAttribute(name = "newHousehold")
    public HouseholdDTO getNewHousehold() {
        return HouseholdDTO.fromEntities(new Household().setOwner(userService.getAuthenticatedUser()).setName(""));
    }

    @ModelAttribute(name = "otherUsers", binding = false)
    public Set<FUser> getOtherUsers() {
        List<FUser> users = userService.getUsers();
        Long currentUserId = userService.getAuthenticatedUser().getId();
        return users.stream().filter(user -> !user.getId().equals(currentUserId)).collect(Collectors.toSet());
    }

}
