package de.moritzjunge.financer.model.dtos;

import de.moritzjunge.financer.model.Category;
import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.Transaction;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private int amount;
    @NotNull
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate transactionDate;
    @DateTimeFormat(pattern = "dd/MM/yyyy hh:mm")
    private LocalDateTime creationDate;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long categoryId;
    @NotNull
    private Long payerId;
    @NotNull
    private Long householdId;

    public static TransactionDTO fromEntities(@NotNull Transaction transaction, @NotNull FUser owner, @NotNull FUser payer, @NotNull Category category, @NotNull Household household) {
        TransactionDTO newDTO = new TransactionDTO();
        newDTO.setId(transaction.getId());
        newDTO.setAmount(transaction.getAmount());
        newDTO.setDescription(transaction.getDescription());
        newDTO.setTransactionDate(transaction.getTransactionDate());
        newDTO.setCreationDate(transaction.getCreationDate());
        newDTO.setOwnerId(owner.getId());
        newDTO.setPayerId(payer.getId());
        newDTO.setCategoryId(category.getId());
        newDTO.setHouseholdId(household.getId());
        return newDTO;
    }

    public Long getId() {
        return id;
    }

    public TransactionDTO setId(Long id) {
        this.id = id;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public TransactionDTO setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public TransactionDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public TransactionDTO setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
        return this;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public TransactionDTO setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public TransactionDTO setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public TransactionDTO setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public Long getPayerId() {
        return payerId;
    }

    public TransactionDTO setPayerId(Long payerId) {
        this.payerId = payerId;
        return this;
    }

    public Long getHouseholdId() {
        return householdId;
    }

    public TransactionDTO setHouseholdId(Long householdId) {
        this.householdId = householdId;
        return this;
    }
}
