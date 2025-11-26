package app;

import service.BankService;
import service.impl.BankServiceImpl;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Console Bank");
        BankService bankService = new BankServiceImpl();
        boolean running = true;
        while (running) {
            System.out.println("""
                1) Open Account
                2) Deposit
                3) Withdraw
                4) Transfer
                5) Account Statement
                6) List Accounts
                7) Search Accounts by Customer Name
                0) Exit
                """);
            System.out.print("CHOOSE : ");
            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().trim();
            System.out.println("CHOICE : " + choice);

            switch (choice) {
                case "1" -> openAccount(scanner, bankService);
                case "2" -> deposit(scanner, bankService);
                case "3" -> withdraw(scanner, bankService);
                case "4" -> transfer(scanner, bankService);
                case "5" -> statement(scanner, bankService);
                case "6" -> listAccounts(scanner, bankService);
                case "7" -> searchAccounts(scanner, bankService);
                case "0" -> running = false;
            }
        }
    }

    private static void openAccount(Scanner scanner, BankService bankService) {
        System.out.println("Customer name : ");
        String name = scanner.nextLine().trim();
        System.out.println("Customer email : ");
        String email = scanner.nextLine().trim();
        System.out.println("Account type (SAVINGS/CURRENT) : ");
        String type = scanner.nextLine().trim();
        System.out.println("Initial deposit (optional, blank for 0) : ");
        String amount = scanner.nextLine().trim();
        Double initial = Double.valueOf(amount);
        String accountNumber = bankService.openAccount(name, email, type);
        if (initial > 0) {
            bankService.deposit(accountNumber, initial, "Initial deposit.");
        }
        System.out.println("Account opened : " + accountNumber);
    }

    private static void deposit(Scanner scanner, BankService bankService) {
        System.out.println("Account number : ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount : ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.deposit(accountNumber, amount, "Deposit");
        System.out.println("Deposited!");
    }

    private static void withdraw(Scanner scanner, BankService bankService) {
        System.out.println("Account number : ");
        String accountNumber = scanner.nextLine().trim();
        System.out.println("Amount : ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.withdraw(accountNumber, amount, "Withdrawal");
        System.out.println("Withdraw!");
    }

    private static void transfer(Scanner scanner, BankService bankService) {
        System.out.println("From Account : ");
        String fromAccount = scanner.nextLine().trim();
        System.out.println("To Account : ");
        String toAccount = scanner.nextLine().trim();
        System.out.println("Amount : ");
        Double amount = Double.valueOf(scanner.nextLine().trim());
        bankService.transfer(fromAccount, toAccount, amount, "Transfer");
    }

    private static void statement(Scanner scanner, BankService bankService) {
        System.out.println("Account number : ");
        String accountNumber = scanner.nextLine().trim();
        bankService.getStatement(accountNumber).forEach(transaction -> {
            System.out.println(transaction.getTimestamp() + " | " + transaction.getType() + " | " + transaction.getAmount() + " | " + transaction.getNote());
        });
    }

    private static void listAccounts(Scanner scanner, BankService bankService) {
        bankService.listAccounts().forEach(account -> {
            System.out.println(account.getAccountNumber() + " | " + account.getAccountType() + " | " + account.getBalance());
        });
    }

    private static void searchAccounts(Scanner scanner, BankService bankService) {
        System.out.println("Customer name contains : ");
        String query = scanner.nextLine().trim();
        bankService.searchAccountsByCustomerName(query);


    }
}
