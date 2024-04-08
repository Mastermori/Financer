package de.moritzjunge.financer.controllers;

import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private TransactionService transactionService;

    public DashboardController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public String getDashboard(Model model, @RequestParam(defaultValue = "") String transactionFilter) {
        List<Transaction> filteredTransactions = transactionService.filterTransactions(transactionFilter);
        model.addAttribute("filter", transactionFilter);
        attachModelAttributes(model, filteredTransactions);
        return "dashboard";
    }

    @PostMapping
    public String createTransaction(Model model, @ModelAttribute Transaction newTransaction) {
//        transactions.add(new Transaction(Integer.parseInt(amount), description, LocalDate.now().minusDays(1), LocalDateTime.now()));
        Transaction stampedTransaction = new Transaction(newTransaction.getAmount(), newTransaction.getDescription(), newTransaction.getTransactionDate(), LocalDateTime.now());
        transactionService.addTransaction(stampedTransaction);
//        attachModelAttributes(model, transactions);
        return "redirect:/dashboard";
    }

    private void attachModelAttributes(Model model, List<Transaction> transactions) {
        model.addAttribute("zeitstempel", LocalDateTime.now().plusHours(10));
        model.addAttribute("transactions", transactions);
        model.addAttribute("newTransaction", new Transaction(100, "", LocalDate.now(), null));
    }

}
