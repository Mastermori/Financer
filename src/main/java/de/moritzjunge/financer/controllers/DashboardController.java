package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.model.dtos.TransactionDTO;
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
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TransactionService transactionService;
    private final HouseholdService householdService;
    private final UserService userService;

    public DashboardController(TransactionService transactionService, HouseholdService householdService, UserService userService) {
        this.transactionService = transactionService;
        this.householdService = householdService;
        this.userService = userService;
    }

    @GetMapping
    public String getDashboard(Model model) {
        List<Transaction> filteredTransactions = transactionService.getTransactions();
        attachModelAttributes(model, filteredTransactions, householdService.getHouseholds());
        return "dashboard";
    }

    @PostMapping
    @Transactional
    public String createTransaction(Model model, @ModelAttribute TransactionDTO newTransactionDTO) {
        Household household = householdService.getHouseholdById(newTransactionDTO.getHouseholdId()).get();
        FUser owner = userService.getUserById(newTransactionDTO.getOwnerId()).get();
        FUser payer = userService.getUserById(newTransactionDTO.getPayerId()).get();
        Category category = householdService.getCategoryById(newTransactionDTO.getCategoryId()).get();
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

    private void attachModelAttributes(Model model, List<Transaction> transactions, Collection<Household> households) {
        Transaction newTransaction = new Transaction(100, "", LocalDate.now(), null);
        FUser currentUser = userService.getAuthenticatedUser();
        Household household = households.iterator().next();
        Set<FUser> possiblePayers = household.getParticipants();

        model.addAttribute("zeitstempel", LocalDateTime.now().plusHours(10));
        model.addAttribute("transactions", transactions);
        model.addAttribute("newTransaction",
                TransactionDTO.fromEntities(newTransaction, currentUser, currentUser, household.getCategories().iterator().next(), household));
        model.addAttribute("categories", household.getCategories());
        model.addAttribute("households", households);
        model.addAttribute("possiblePayers", possiblePayers);
    }

}
