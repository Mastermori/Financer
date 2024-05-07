package de.moritzjunge.financer.model.dtos;

import de.moritzjunge.financer.model.FUser;

import java.util.HashMap;
import java.util.List;

public class DebtContext {

    private List<Debt> debts;
    private HashMap<FUser, Integer> userSpendings;
    private int totalSpendings;
    private int transactionAmount;

    public DebtContext(List<Debt> debts, HashMap<FUser, Integer> userSpendings, int totalSpendings, int transactionAmount) {
        this.debts = debts;
        this.userSpendings = userSpendings;
        this.totalSpendings = totalSpendings;
        this.transactionAmount = transactionAmount;
    }

    public List<Debt> getDebts() {
        return debts;
    }

    public void setDebts(List<Debt> debts) {
        this.debts = debts;
    }

    public int getTotalSpendings() {
        return totalSpendings;
    }

    public void setTotalSpendings(int totalSpendings) {
        this.totalSpendings = totalSpendings;
    }

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public HashMap<FUser, Integer> getUserSpendings() {
        return userSpendings;
    }

    public void setUserSpendings(HashMap<FUser, Integer> userSpendings) {
        this.userSpendings = userSpendings;
    }
}
