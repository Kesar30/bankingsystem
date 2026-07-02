package OnlineBankingSystem.com;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class CustomerMenu {

//    private Balance balance;
    private Scanner sc = new Scanner(System.in);
    private Connection con;
    private balance bl;
    private password ps;
    public CustomerMenu(Connection con,balance bl,password ps) {
        this.con = con;
        this.bl = bl;
        this.ps = ps;
    }
    public void showMenu(int accountNo , String password, int id) throws SQLException {

        boolean running = true;
        while (running) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Money");
            System.out.println("5. Transaction History");
            System.out.println("6. Change Password");
            System.out.println("7. Logout");
            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    bl.getbalance(accountNo);
                    break;
                case 2:
                    bl.deposit(accountNo);
                    break;
                case 3:
                    bl.withdraw(accountNo);
                    break;
                case 4:
                    bl.transaction(accountNo);
                    break;
                case 5:
                    bl.getTransaction(accountNo);
                    break;
                case 6:
                    ps.changePassword(id,password);
                    break;
                case 7:
                    running = false;
                    System.out.println("Logout Successful");
                    break;
                default:
                    System.out.println("Invalid Choice");
            }

        }

    }

}
