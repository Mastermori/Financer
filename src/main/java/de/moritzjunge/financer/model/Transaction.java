package de.moritzjunge.financer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

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

    private int amount;
    private String description;
    private LocalDate transactionDate;
    private LocalDateTime creationDate;

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

    public void setId(Long id) {
        this.id = id;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setTransactionDate(String transactionDateString) {
        transactionDate = LocalDate.parse(transactionDateString);
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Transaction) obj;
        return this.amount == that.amount &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.transactionDate, that.transactionDate) &&
                Objects.equals(this.creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, description, transactionDate, creationDate);
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
