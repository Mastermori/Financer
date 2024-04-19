package de.moritzjunge.financer.services;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.repositories.CategoryRepository;
import de.moritzjunge.financer.repositories.HouseholdRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

@Service
public class HouseholdService {

    private final HouseholdRepository householdRepository;
    private final UserService userService;

    public HouseholdService(HouseholdRepository householdRepository, UserService userService) {
        this.householdRepository = householdRepository;
        this.userService = userService;

//        Category cat1 = new Category().setDescription("Test1 H").setColor(Color.RED);
//        Category cat2 = new Category().setDescription("Test2 H").setColor(Color.BLUE);
//
//        FUser owner = userService.getUserById(1L).get();
//
//        Household household = new Household().setName("Test Household").setOwner(owner);
//
//        household.addCategory(cat1);
//        household.addCategory(cat2);
//
//        addHousehold(household);
//
//        household.addParticipant(owner);
//
//        categoryRepository.save(cat1);
//        categoryRepository.save(cat2);
    }

    public Optional<Household> getHouseholdById(Long id) {
        return householdRepository.findById(id);
    }

    public List<Household> getHouseholds() {
        return householdRepository.findAll();
    }

    public List<Household> getHouseholdsForUser(FUser user) {
        return householdRepository.findAllByOwnerId(user.getId());
    }

    public void addHousehold(Household newHousehold) {
        householdRepository.save(newHousehold);
    }

    public boolean householdExists(Long id) {
        return householdRepository.existsById(id);
    }

}
