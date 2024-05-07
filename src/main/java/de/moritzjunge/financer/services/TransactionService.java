package de.moritzjunge.financer.services;

import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactions;

    public Optional<Transaction> getTransactionById(long id) {
        return transactions.findById(id);
    }

    public List<Transaction> getTransactions() {
        return transactions.findAll();
    }

    public List<Transaction> filterTransactions(@NonNull String filterText) {
        if (!filterText.isEmpty()) {
            return getTransactions().stream().filter((transaction -> transaction.getDescription().toLowerCase().contains(filterText.toLowerCase()))).toList();
        }
        return getTransactions();
    }

    public void addTransaction(Transaction newTransaction) {
        transactions.save(newTransaction);
    }

    public void removeTransactionById(Long id) {
        transactions.deleteById(id);
    }

}
