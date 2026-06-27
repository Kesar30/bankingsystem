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

            // create user object
            user USER = new user(con);

            boolean running = true;
            while (running) {
                System.out.println("******** SELECT OPTION ********");
                System.out.println("1. Register Account");
                System.out.println("2. Login Account");
                System.out.println("0. Exit");
                System.out.print("Enter Your Choice (0-10): ");

                int choice = sc.nextInt();
                switch (choice) {
                    case 1:
                        USER.registeruser();
                        break;
                    case 2:
                        USER.loginuser();
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        running = false; // exit loop
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }

                System.out.print("Do you want to continue (Y/N): ");
                String end = sc.next();
                if (end.equalsIgnoreCase("N")) {
                    running = false;
                }
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
