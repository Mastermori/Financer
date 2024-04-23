package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.model.dtos.HouseholdDTO;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    public String getDashboard(Model model, @RequestParam(required = false, defaultValue = "") String householdId, @RequestParam(required = false, defaultValue = "") String editTransactionId) {
        List<Transaction> filteredTransactions = transactionService.getTransactions();
        if (!householdId.isEmpty()) {
            filteredTransactions = filteredTransactions.stream().filter(household -> household.getId().equals(Long.parseLong(householdId))).toList();
        }

        attachNewTransaction(model);
        attachModelAttributes(model, filteredTransactions);
        model.addAttribute("selectedHouseholdId", householdId);

        if (!editTransactionId.isEmpty()) {
            Optional<Transaction> editTransaction = transactionService.getTransactionById(Long.parseLong(editTransactionId));
            editTransaction.ifPresent(transaction -> model.addAttribute("editTransaction", TransactionDTO.fromEntities(transaction)));
        }

        return "dashboard";
    }

    @PostMapping("/edit")
    public String editTransaction(Model model, @Valid @ModelAttribute(name = "editTransaction") TransactionDTO editedTransactionDTO, BindingResult bindingResult) {
        Optional<Transaction> transactionOptional = transactionService.getTransactionById(editedTransactionDTO.getId());
        if (transactionOptional.isEmpty()) {
            return "redirect:/dashboard";
        }
        validateTransactionDTO(editedTransactionDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            List<Transaction> filteredTransactions = transactionService.getTransactions();
            attachNewTransaction(model);
            attachModelAttributes(model, filteredTransactions);
            model.addAttribute("editTransaction", editedTransactionDTO);
            return "dashboard";
        }
        Transaction transaction = transactionOptional.get();
        transaction.setAmount(editedTransactionDTO.getAmount());
        transaction.setDescription(editedTransactionDTO.getDescription());
        transaction.setTransactionDate(editedTransactionDTO.getTransactionDate());
        if (!Objects.equals(transaction.getCategory().getId(), editedTransactionDTO.getCategoryId())) {
            Category category = categoryService.getCategoryById(editedTransactionDTO.getCategoryId()).get();
            transaction.setCategory(category);
        }
        if (!Objects.equals(transaction.getPayer().getId(), editedTransactionDTO.getPayerId())) {
            FUser payer = userService.getUserById(editedTransactionDTO.getPayerId()).get();
            transaction.setPayer(payer);
        }
        return "redirect:/dashboard";
    }

    @PostMapping("/delete/{id}")
    public String deleteTransaction(Model model, @PathVariable(name = "id") String idString) {
        Long id = Long.parseLong(idString);
        transactionService.removeTransactionById(id);
        return "redirect:/dashboard";
    }

    @PostMapping
    public String createTransaction(Model model, @Valid @ModelAttribute(name = "newTransaction") TransactionDTO newTransactionDTO, BindingResult bindingResult) {
        validateTransactionDTO(newTransactionDTO, bindingResult);
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

    private void validateTransactionDTO(TransactionDTO transactionDTO, BindingResult bindingResult) {
        if (!bindingResult.hasFieldErrors("householdId") && !householdService.householdExists(transactionDTO.getHouseholdId())) {
            bindingResult.rejectValue("householdId", "household.id.missing", "could not find that household. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("ownerId") && !userService.userExists(transactionDTO.getOwnerId())) {
            bindingResult.rejectValue("ownerId", "owner.id.missing", "could not find that user. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("payerId") && !userService.userExists(transactionDTO.getPayerId())) {
            bindingResult.rejectValue("payerId", "payer.id.missing", "could not find that user. Try refreshing");
        }
        if (!bindingResult.hasFieldErrors("categoryId") && !categoryService.categoryExists(transactionDTO.getCategoryId())) {
            bindingResult.rejectValue("categoryId", "category.id.missing", "could not find that category. Try refreshing");
        }
        if (transactionDTO.getAmount() == 0) {
            bindingResult.rejectValue("amount", "amount.zero", "cannot be zero");
        }
    }

    private void attachNewTransaction(Model model) {
        Transaction newTransaction = new Transaction(100, "", LocalDate.now(), null);
        FUser currentUser = userService.getAuthenticatedUser();
        Set<Household> households = currentUser.getParticipatingHouseholds();
        Household household = households.isEmpty() ? new Household().setId(-1L).setName("No Household") : households.iterator().next();
        Category selectedCategory = household.getId() == -1 ? new Category().setDescription("No categories") : household.getCategories().iterator().next();

        model.addAttribute("newTransaction",
                TransactionDTO.fromEntities(newTransaction, currentUser, currentUser, selectedCategory, household));
    }

    private void attachModelAttributes(Model model, List<Transaction> transactions) {
        FUser currentUser = userService.getAuthenticatedUser();
        Set<Household> households = currentUser.getParticipatingHouseholds();
        Household household = households.isEmpty() ? null : households.iterator().next();
        Set<FUser> possiblePayers = new HashSet<>(userService.getUsers());

        model.addAttribute("categories", household != null ? household.getCategories() : new HashSet<>());
        model.addAttribute("households", households.stream().map(HouseholdDTO::fromEntities).collect(Collectors.toSet()));
        model.addAttribute("possiblePayers", possiblePayers);
        model.addAttribute("timestamp", LocalDate.now());
        model.addAttribute("transactions", transactions);
    }

}
