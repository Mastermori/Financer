package de.moritzjunge.financer.model.dtos;

import de.moritzjunge.financer.model.FUser;

public class Debt {

    private FUser debtor;
    private FUser creditor;
    private int amount;

    public Debt(FUser debtor, FUser creditor, int amount) {
        this.debtor = debtor;
        this.creditor = creditor;
        this.amount = amount;
    }

    public FUser getDebtor() {
        return debtor;
    }

    public void setDebtor(FUser debtor) {
        this.debtor = debtor;
    }

    public FUser getCreditor() {
        return creditor;
    }

    public void setCreditor(FUser creditor) {
        this.creditor = creditor;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Debt{" +
                "debtor=" + debtor +
                ", creditor=" + creditor +
                ", amount=" + amount +
                '}';
    }
}
