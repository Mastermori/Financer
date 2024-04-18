package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.model.dtos.TransactionDTO;
import de.moritzjunge.financer.services.CategoryService;
import de.moritzjunge.financer.services.HouseholdService;
import de.moritzjunge.financer.services.TransactionService;
import de.moritzjunge.financer.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dashboard")
@Transactional
public class DashboardController {

    private final TransactionService transactionService;
    private final HouseholdService householdService;
    private final CategoryService categoryService;
    private final UserService userService;

    public DashboardController(TransactionService transactionService, HouseholdService householdService, CategoryService categoryService, UserService userService) {
        this.transactionService = transactionService;
        this.householdService = householdService;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping
    public String getDashboard(Model model) {
        List<Transaction> filteredTransactions = transactionService.getTransactions();
        attachModelAttributes(model, filteredTransactions);
        return "dashboard";
    }

    @PostMapping
    public String createTransaction(Model model, @ModelAttribute TransactionDTO newTransactionDTO) {
        Household household = householdService.getHouseholdById(newTransactionDTO.getHouseholdId()).get();
        FUser owner = userService.getUserById(newTransactionDTO.getOwnerId()).get();
        FUser payer = userService.getUserById(newTransactionDTO.getPayerId()).get();
        Category category = categoryService.getCategoryById(newTransactionDTO.getCategoryId()).get();
        Transaction stampedTransaction = new Transaction(
                newTransactionDTO.getAmount(),
                newTransactionDTO.getDescription(),
                newTransactionDTO.getTransactionDate(),
                LocalDateTime.now()
        )
                .setHousehold(household)
                .setOwner(owner)
                .setPayer(payer)
                .setCategory(category);
        transactionService.addTransaction(stampedTransaction);
        return "redirect:/dashboard";
    }

    private void attachModelAttributes(Model model, List<Transaction> transactions) {
        Transaction newTransaction = new Transaction(100, "", LocalDate.now(), null);
        FUser currentUser = userService.getAuthenticatedUser();
        Set<Household> households = currentUser.getParticipatingHouseholds();
        Household household = households.isEmpty() ? new Household().setId(-1L).setName("No Household") : households.iterator().next();
        Set<FUser> possiblePayers = household.getId() == -1 ? new HashSet<>() : household.getParticipants();
        Category selectedCategory = household.getId() == -1 ? new Category().setDescription("No categories") : household.getCategories().iterator().next();

        model.addAttribute("zeitstempel", LocalDateTime.now().plusHours(10));
        model.addAttribute("transactions", transactions);
        model.addAttribute("newTransaction",
                TransactionDTO.fromEntities(newTransaction, currentUser, currentUser, selectedCategory, household));
        model.addAttribute("categories", household.getCategories());
        model.addAttribute("households", households);
        model.addAttribute("possiblePayers", possiblePayers);
    }

}
