package service.impl;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repository.AccountRepository;
import repository.CustomerRepository;
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
    private final CustomerRepository customerRepository = new CustomerRepository();

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
    public void deposit(String accountNumber, Double amount, String depositNote) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found " + accountNumber));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.DEPOSIT, account.getAccountNumber(), amount, LocalDateTime.now(), depositNote);
        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String withdrawalNote) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found " + accountNumber));
        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(UUID.randomUUID().toString(), Type.WITHDRAW, account.getAccountNumber(), amount, LocalDateTime.now(), withdrawalNote);
        transactionRepository.add(transaction);
    }

    @Override
    public void transfer(String fromAccount, String toAccount, Double amount, String transferNote) {
        if (fromAccount.equals(toAccount)) {
            throw new RuntimeException("Cannot transfer to your own account");
        }
        Account fromAcc = accountRepository.findByAccountNumber(fromAccount).orElseThrow(() -> new RuntimeException("Account not found " + fromAccount));
        Account toAcc = accountRepository.findByAccountNumber(toAccount).orElseThrow(() -> new RuntimeException("Account not found " + toAccount));
        if (fromAcc.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient Balance");
        }

        fromAcc.setBalance(fromAcc.getBalance() - amount);
        toAcc.setBalance(toAcc.getBalance() + amount);

        Transaction fromTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_OUT, fromAcc.getAccountNumber(), amount, LocalDateTime.now(), transferNote);
        transactionRepository.add(fromTransaction);

        Transaction toTransaction = new Transaction(UUID.randomUUID().toString(), Type.TRANSFER_IN, toAcc.getAccountNumber(), amount, LocalDateTime.now(), transferNote);
        transactionRepository.add(toTransaction);

    }

    @Override
    public List<Transaction> getStatement(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber)
                .stream()
                .sorted(Comparator.comparing(Transaction::getTimestamp))
                .collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query = q == null ? "" : q.toLowerCase();

        return customerRepository.findAll()
                .stream()
                .filter(customer -> customer.getName().toLowerCase().contains(query))
                .flatMap(customer -> accountRepository.findByCustomerId(customer.getId())
                        .stream())
                        .sorted(Comparator.comparing(Account::getAccountNumber))
                        .collect(Collectors.toList());
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size() + 1;
        return String.format("AC%06d", size);
    }
}