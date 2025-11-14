package app;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Welcome to Console Bank");
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
    }
}
