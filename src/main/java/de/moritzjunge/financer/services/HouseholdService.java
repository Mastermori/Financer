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

    public HouseholdService(HouseholdRepository householdRepository) {
        this.householdRepository = householdRepository;
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
