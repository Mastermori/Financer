package de.moritzjunge.financer;


import de.moritzjunge.financer.model.FUser;
import de.moritzjunge.financer.model.Household;
import de.moritzjunge.financer.model.Transaction;
import de.moritzjunge.financer.model.dtos.Debt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HouseholdTest {

    private FUser user1;
    private FUser user2;
    private LocalDate date;

    private void testTwoUsers(List<Transaction> transactions, int expectedDeviation) {
        Household household = new Household();
        transactions.forEach(household::addTransaction);
        List<Debt> debts = household.calculateDebtForTimeframe(date, date);
        assertEquals(1, debts.size(), "Too many debts created for simple scenario");
        assertEquals(expectedDeviation, debts.get(0).getAmount(), "Too many debts created for simple scenario");
        assertEquals(user1, debts.get(0).getDebtor(), "Wrong debtor");
        assertEquals(user2, debts.get(0).getCreditor(), "Wrong creditor");
    }

    @BeforeEach
    public void createUsersAndDate() {
        user1 = new FUser().setName("User1");
        user2 = new FUser().setName("User2");
        date = LocalDate.of(2024, 4, 4);
    }

    @Test
    public void testDebtDistributionTwoUsers() {
        List<Transaction> transactions = List.of(
                new Transaction().setTransactionDate(date).setAmount(100).setPayer(user1).setId(1L),
                new Transaction().setTransactionDate(date).setAmount(500).setPayer(user2).setId(2L)
        );
        testTwoUsers(transactions, 200);
    }

    @Test
    public void testDebtDistributionTwoUsersDateBefore() {
        List<Transaction> transactions = List.of(
                new Transaction().setTransactionDate(date).setAmount(100).setPayer(user1).setId(1L),
                new Transaction().setTransactionDate(date).setAmount(500).setPayer(user2).setId(2L),
                new Transaction().setTransactionDate(date.minusDays(1)).setPayer(user2).setId(3L)
        );
        testTwoUsers(transactions, 200);
    }

    @Test
    public void testDebtDistributionTwoUsersDateAfter() {
        List<Transaction> transactions = List.of(
                new Transaction().setTransactionDate(date).setAmount(100).setPayer(user1).setId(1L),
                new Transaction().setTransactionDate(date).setAmount(500).setPayer(user2).setId(2L),
                new Transaction().setTransactionDate(date.plusDays(1)).setPayer(user2).setId(3L)
        );
        testTwoUsers(transactions, 200);
    }

    @Test
    public void testDebtDistributionThreeUsers() {
        FUser user3 = new FUser().setName("User3");
        List<Transaction> transactions = List.of(
                new Transaction().setTransactionDate(date).setAmount(500).setPayer(user1).setId(1L),
                new Transaction().setTransactionDate(date).setAmount(800).setPayer(user2).setId(2L),
                new Transaction().setTransactionDate(date).setAmount(1700).setPayer(user3).setId(3L)
        );
        Household household = new Household();
        transactions.forEach(household::addTransaction);
        List<Debt> debts = household.calculateDebtForTimeframe(date, date);
        Optional<Debt> user1Debt = debts.stream().filter(debt -> debt.getDebtor().equals(user1)).findFirst();
        Optional<Debt> user1Credit = debts.stream().filter(debt -> debt.getCreditor().equals(user1)).findFirst();
        Optional<Debt> user2Debt = debts.stream().filter(debt -> debt.getDebtor().equals(user2)).findFirst();
        Optional<Debt> user2Credit = debts.stream().filter(debt -> debt.getCreditor().equals(user2)).findFirst();
        Optional<Debt> user3Debt = debts.stream().filter(debt -> debt.getDebtor().equals(user3)).findFirst();
        assertEquals(true, user1Debt.isPresent(), "Debt is not correctly created for user1");
        assertEquals(false, user1Credit.isPresent(), "Credit is falsely created for user1");
        assertEquals(true, user2Debt.isPresent(), "Debt is not correctly created for user2");
        assertEquals(false, user2Credit.isPresent(), "Credit is falsely created for user2");
        assertEquals(false, user3Debt.isPresent(), "Debt is falsely created for user3");
        assertEquals(500, user1Debt.get().getAmount());
        assertEquals(user3, user1Debt.get().getCreditor());
        assertEquals(200, user2Debt.get().getAmount());
        assertEquals(user3, user2Debt.get().getCreditor());
    }

}
