package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.dtos.UserDTO;
import de.moritzjunge.financer.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
//@RequestMapping("/")
public class MainController {

//    @GetMapping
//    public String get() {
//        return "redirect:/dashboard";
//    }

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDTO newUser = new UserDTO();
        model.addAttribute("user", newUser);
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDTO userDTO, BindingResult result, Model model) {
        FUser existingUser = userService.findUserByName(userDTO.getName());

        if (existingUser != null && existingUser.getName() != null) {
            result.rejectValue("name", "409", "There is already an account reigstered with the same name");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDTO);
            return "register";
        }

        userService.register(userDTO);
        return "redirect:/dashboard";
    }
}
