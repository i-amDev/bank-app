package service.impl;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.TransactionRepository;
import service.BankService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();

    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();

        // TODO Will change the logic for generating account number later.
//        String accountNumber = UUID.randomUUID().toString();

        String accountNumber = getAccountNumber();

        Account account = new Account(accountType, (double) 0, customerId, accountNumber);

        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream()
                .sorted(Comparator.comparing(Account::getAccountNumber))
                .collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String deposit) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.DEPOSIT, account.getAccountNumber(), amount, LocalDateTime.now(), deposit);
        transactionRepository.add(transaction);
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d", size);
    }
}
