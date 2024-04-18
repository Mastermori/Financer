package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.services.UserService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

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

}
