package repository;

import domain.Transaction;

import java.util.*;

public class TransactionRepository {

    private final Map<String, List<Transaction>> transactionByAccount = new HashMap<>();

    public void add(Transaction transaction) {
        transactionByAccount.computeIfAbsent(transaction.getAccountNumber(), k -> new ArrayList<>()).add(transaction);
    }

    public List<Transaction> findByAccountNumber(String accountNumber) {
        return new ArrayList<>(transactionByAccount.getOrDefault(accountNumber, Collections.emptyList()));
    }

}
