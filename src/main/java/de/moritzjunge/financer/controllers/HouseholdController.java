package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.dtos.HouseholdDTO;
import de.moritzjunge.financer.services.CategoryService;
import de.moritzjunge.financer.services.HouseholdService;
import de.moritzjunge.financer.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/households")
@Transactional
public class HouseholdController {

    private final HouseholdService householdService;
    private final CategoryService categoryService;
    private final UserService userService;

    public HouseholdController(HouseholdService householdService, CategoryService categoryService, UserService userService) {
        this.householdService = householdService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String getHouseholdCreation(Model model) {
        FUser currentUser = userService.getAuthenticatedUser();
        Household newHousehold = new Household().setOwner(currentUser).setName("");

        List<FUser> possibleParticipants = userService.getUsers();
        possibleParticipants.remove(currentUser);

        model.addAttribute("newHousehold", HouseholdDTO.fromEntities(newHousehold));
        model.addAttribute("possibleParticipants", possibleParticipants);
        return "new-household";
    }

    @PostMapping("/new")
    public String createHousehold(Model model, @ModelAttribute HouseholdDTO newHouseholdDTO) {
        FUser owner = userService.getUserById(newHouseholdDTO.getOwnerId()).get();
        Household household = new Household()
                .setOwner(owner)
                .setName(newHouseholdDTO.getName());
        householdService.addHousehold(household);

        household.addParticipant(owner);
        for (Long userId : newHouseholdDTO.getParticipantIds()) {
            Optional<FUser> participant = userService.getUserById(userId);
            if (participant.isEmpty()) {
                continue;
            }
            household.addParticipant(participant.get());
        }

        Category cat1 = new Category().setDescription("Test1 H").setColor(Color.RED);
        Category cat2 = new Category().setDescription("Test2 H").setColor(Color.BLUE);

        household.addCategory(cat1);
        household.addCategory(cat2);

        categoryService.addCategory(cat1);
        categoryService.addCategory(cat2);

        return "redirect:/households/" + household.getId();
    }

    @GetMapping("/{id}")
    public String getHousehold(Model model, @PathVariable Long id) {
        return "index";
    }
}
