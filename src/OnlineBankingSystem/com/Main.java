package OnlineBankingSystem.com;
import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // load driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // create dbconnection
            dbconnection db = new dbconnection();
            Connection con = db.createconnection();
// create balance class object
            balance BALANCE = new balance(con);
            //CREATING password class object
            // create user class object
            password PASSWORD = new password(con);
            //creating menu class object
            CustomerMenu MENU = new CustomerMenu(con,BALANCE,PASSWORD);
            // create user class object
            user USER = new user(con,MENU);
            // creating admin class object
            admin ADMIN = new admin(con);


            //creating menu for functions
            boolean running = true;
            while (running) {

                System.out.println("******** ONLINE BANKING ********");
                System.out.println("1. Customer Register");
                System.out.println("2. Customer Login");
                System.out.println("3. Admin Login");
                System.out.println("0. Exit");
                System.out.println("Enter Your Choice: ");
                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        USER.registeruser();
                        break;
                    case 2:
                        USER.loginuser();  // After successful login it will open CustomerMenu
                        break;
                    case 3:
                       ADMIN.adminLogin();
                        break;
                    case 0:
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid Choice");
                }
                // 👉 Only ask to continue if still in main menu loop
                /*if (running && choice != 2) {
                    System.out.print("Do you want to continue (Y/N): ");
                    String end = sc.next();
                    if (end.equalsIgnoreCase("N")) {
                        running = false;
                    }
                }*/
            }
            // close connection after loop ends
            con.close();

        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
