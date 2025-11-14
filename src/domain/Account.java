package domain;

public class Account {

    private String accountNumber;

    private String customerId;

    private Double balance;

    private String accountType;

    public Account(String accountType, Double balance, String customerId, String accountNumber) {
        this.accountType = accountType;
        this.balance = balance;
        this.customerId = customerId;
        this.accountNumber = accountNumber;
    }
}
