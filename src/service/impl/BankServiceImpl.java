package service.impl;

import domain.Account;
import repository.AccountRepository;
import service.BankService;

import java.util.UUID;

public class BankServiceImpl implements BankService {

    private final AccountRepository accountRepository = new AccountRepository();

    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();

        // TODO Will change the logic for generating account number later.
        String accountNumber = UUID.randomUUID().toString();

        Account account = new Account(accountType, (double) 0, customerId, accountNumber);

        accountRepository.save(account);

        return accountNumber;
    }

}
