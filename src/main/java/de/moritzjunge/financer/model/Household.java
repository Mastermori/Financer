package de.moritzjunge.financer.model;

import de.moritzjunge.financer.model.dtos.Debt;
import de.moritzjunge.financer.model.dtos.DebtContext;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.time.LocalDate;
import java.util.*;

@Entity
public class Household {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", nullable = false)
    private FUser owner;
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(name = "householdParticipant", joinColumns = @JoinColumn(name = "householdId"), inverseJoinColumns = @JoinColumn(name = "usersId"))
    private Set<FUser> participants = new HashSet<>();
    @OneToMany(mappedBy = "household")
    private SortedSet<Category> categories = new TreeSet<>();
    @OneToMany(mappedBy = "household")
    private Set<Transaction> transactions = new HashSet<>();

    public DebtContext calculateDebtForTimeframe(LocalDate startDate, LocalDate endDate) {
        List<Debt> debts = new ArrayList<>();
        HashMap<FUser, Integer> spendings = new HashMap<>();
        int transactionCount = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionDate().isBefore(startDate) || transaction.getTransactionDate().isAfter(endDate))
                continue;
            transactionCount++;
            spendings.merge(transaction.getPayer(), transaction.getAmount(), Integer::sum);
        }
        double userWeight = 1.0 / spendings.keySet().size();
        int totalSpendings = spendings.values().stream().reduce(0, Integer::sum);
        HashMap<FUser, Integer> userDebt = new HashMap<>();
        HashMap<FUser, Integer> userCredit = new HashMap<>();
        for (var spending : spendings.entrySet()) {
            double amountOwed = totalSpendings * userWeight - spending.getValue();
            if (amountOwed > 0) {
                userDebt.put(spending.getKey(), (int) amountOwed);
            } else if (amountOwed < 0) {
                userCredit.put(spending.getKey(), (int) -amountOwed);
            }
        }
        int creditorCount = userCredit.size();
        for (var debt : userDebt.entrySet()) {
            for (var credit : userCredit.entrySet()) {
                int owedAmount = Math.min(debt.getValue() / creditorCount, credit.getValue());
                debt.setValue(debt.getValue() - owedAmount);
                credit.setValue(credit.getValue() - owedAmount);
                if (credit.getValue() < 1) {
                    creditorCount--;
                }
                debts.add(new Debt(debt.getKey(), credit.getKey(), owedAmount));
            }
        }
        return new DebtContext(debts, spendings, totalSpendings, transactionCount);
    }

    public void addTransaction(Transaction transaction) {
        transaction.setHousehold(this);
        transactions.add(transaction);
    }

    public void addParticipant(FUser user) {
        participants.add(user);
        user.getParticipatingHouseholds().add(this);
    }

    public void removeParticipant(FUser user) {
        participants.remove(user);
        user.getParticipatingHouseholds().remove(this);
    }

    public void clearParticipants() {
        for (FUser user : new ArrayList<>(getParticipants())) {
            if (user.getId().equals(owner.getId())) {
                continue;
            }
            removeParticipant(user);
        }
    }

    public void addCategory(Category category) {
        categories.add(category);
        category.setHousehold(this);
    }

    public void removeCategory(Category category) {
        categories.remove(category);
        category.setHousehold(null);
    }

    public String getName() {
        return name;
    }

    public Household setName(String name) {
        this.name = name;
        return this;
    }

    public Long getId() {
        return id;
    }

    public FUser getOwner() {
        return owner;
    }

    public Set<FUser> getParticipants() {
        return participants;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public Household setId(Long id) {
        this.id = id;
        return this;
    }

    public Household setOwner(FUser owner) {
        this.owner = owner;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Household other))
            return false;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
