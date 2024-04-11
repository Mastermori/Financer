package de.moritzjunge.financer.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;

@Entity
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int amount;
    @Column(nullable = false)
    private String description;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate transactionDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime creationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ownerId", nullable = false)
    private FUser owner;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId", nullable = false)
    private Category category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payerId", nullable = false)
    private FUser payer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "householdId", nullable = false)
    private Household household;



    // Required by Hibernate
    public Transaction() {
    }

    public Transaction(
            int amount, String description, LocalDate transactionDate, LocalDateTime creationDate
    ) {
        this.amount = amount;
        this.description = description;
        this.transactionDate = transactionDate;
        this.creationDate = creationDate;
    }

    public String getFormattedAmount() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        return formatter.format(amount / 100.0);
    }

    public Long getId() {
        return id;
    }

    public int getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public FUser getOwner() {
        return owner;
    }

    public Category getCategory() {
        return category;
    }

    public FUser getPayer() {
        return payer;
    }

    public Household getHousehold() {
        return household;
    }

    public Transaction setId(Long id) {
        this.id = id;
        return this;
    }

    public Transaction setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Transaction setDescription(String description) {
        this.description = description;
        return this;
    }

    public Transaction setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public Transaction setOwner(FUser owner) {
        this.owner = owner;
        return this;
    }

    public Transaction setTransactionDate(String transactionDateString) {
        transactionDate = LocalDate.parse(transactionDateString);
        return this;
    }

    public Transaction setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Transaction setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Transaction setPayer(FUser payer) {
        this.payer = payer;
        return this;
    }

    public Transaction setHousehold(Household household) {
        this.household = household;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction[" +
                "id=" + id + ", " +
                "amount=" + amount + ", " +
                "description=" + description + ", " +
                "transactionDate=" + transactionDate + ", " +
                "creationDate=" + creationDate + ']';
    }

}
