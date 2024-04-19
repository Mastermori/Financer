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
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public String getDashboard(Model model, @RequestParam(required = false, defaultValue = "") String householdId) {
        List<Transaction> filteredTransactions = transactionService.getTransactions();
        if (!householdId.isEmpty()) {
            filteredTransactions = filteredTransactions.stream().filter(household -> household.getId().equals(Long.parseLong(householdId))).toList();
        }
        Transaction newTransaction = new Transaction(100, "", LocalDate.now(), null);
        FUser currentUser = userService.getAuthenticatedUser();
        Set<Household> households = currentUser.getParticipatingHouseholds();
        Household household = households.isEmpty() ? new Household().setId(-1L).setName("No Household") : households.iterator().next();
        Category selectedCategory = household.getId() == -1 ? new Category().setDescription("No categories") : household.getCategories().iterator().next();
        Set<FUser> possiblePayers = household.getId() == -1 ? new HashSet<>() : household.getParticipants();

        model.addAttribute("newTransaction",
                TransactionDTO.fromEntities(newTransaction, currentUser, currentUser, selectedCategory, household));
        model.addAttribute("categories", household.getCategories());
        model.addAttribute("households", households);
        model.addAttribute("possiblePayers", possiblePayers);
        attachModelAttributes(model, filteredTransactions);
        model.addAttribute("selectedHouseholdId", householdId);
        return "dashboard";
    }

    @PostMapping
    public String createTransaction(Model model, @Valid @ModelAttribute(name = "newTransaction") TransactionDTO newTransactionDTO, BindingResult bindingResult, @ModelAttribute HashSet<Category> categories, @ModelAttribute HashSet<Household> households, @ModelAttribute HashSet<FUser> possiblePayers) {
        if (!bindingResult.hasFieldErrors("householdId") && !householdService.householdExists(newTransactionDTO.getHouseholdId())) {
            bindingResult.rejectValue("householdId", "household.id.missing", "could not find that household. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("ownerId") && !userService.userExists(newTransactionDTO.getOwnerId())) {
            bindingResult.rejectValue("ownerId", "owner.id.missing", "could not find that user. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("payerId") && !userService.userExists(newTransactionDTO.getPayerId())) {
            bindingResult.rejectValue("payerId", "payer.id.missing", "could not find that user. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("categoryId") && !categoryService.categoryExists(newTransactionDTO.getCategoryId())) {
            bindingResult.rejectValue("categoryId", "category.id.missing", "could not find that category. Try refreshing");
        }
        if(newTransactionDTO.getAmount() == 0) {
            bindingResult.rejectValue("amount", "amount.zero", "cannot be zero");
        }
        if (bindingResult.hasErrors()) {
            List<Transaction> filteredTransactions = transactionService.getTransactions();
            attachModelAttributes(model, filteredTransactions);
            return "dashboard";
        }
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
        model.addAttribute("zeitstempel", LocalDateTime.now().plusHours(10));
        model.addAttribute("transactions", transactions);
    }

}
