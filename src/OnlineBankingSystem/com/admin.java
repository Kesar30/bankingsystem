package OnlineBankingSystem.com;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
public class admin {
    private Connection con ;
//    private CustomerMenu mn;
    admin(Connection con){
        this.con = con ;
//        this.mn = mn;

    }
    Scanner sc =  new Scanner(System.in);
    public void adminLogin() throws SQLException{
        String query = "SELECT username,password FROM admin_db";
        PreparedStatement ps = con.prepareStatement(query);
        System.out.print("Enter the Username : ");
        String username = sc.next();
        System.out.println("Enter the Password");
        String password  = sc.next();
        ResultSet rs = ps.executeQuery();
        while (rs.next()){
            String user_db = rs.getString("username");
            String pwd_db = rs.getString("password");
            if(username.equals(user_db) && password.equals(pwd_db)){
                System.out.println("Login Successful");
                adminMenu();
            }
            else{
               System.out.println("Login Failed \nCheck Username and Password again");
            }
        }
    }

    public void adminMenu() throws SQLException{
        boolean running = true;
        while (running){
            System.out.println("1. View All Customers");
            System.out.println("2. Search Customer");
            System.out.println("3. Delete Customer");
            System.out.println("4. View All Transactions");
            System.out.println("5. Logout");
            System.out.println("Enter Choice : ");
            int choice = sc.nextInt();
            switch(choice){
                case 1 :
                    viewCustomer();
                    break;

                case 2 :
                    searchCustomer();
                    break;

                case 3 :
                    deleteCustomer();
                    break;

                case 4 :
                    viewTransactions();
                    break;

                case 5 :
                    running = false;
                    System.out.println("Logout Successful");
                    break;

                default:
                    System.out.println("Invalid Choice");
            }
        }
    }
    //method to view all customer
    public void viewCustomer() throws SQLException{
        String query = "SELECT users.*,accounts.account_no,accounts.account_type,accounts.balance FROM users LEFT JOIN accounts ON users.user_id = accounts.user_id;";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs =ps.executeQuery();
        String format = "| %-8s | %-12s | %-20s | %-12s | %-12s | %-12s | %-10s |%n";
        String line = "+----------+--------------+----------------------+--------------+--------------+--------------+------------+";
        System.out.println(line);//table header
        System.out.printf(format, "User_id" , "Full_Name" ,"Email" , "Password" ,"Account No" ,"Type","Balance");
        System.out.println(line);
        while(rs.next()){
            System.out.printf(format,rs.getInt("user_id"),
                    rs.getString("Full_Name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("account_no"),
                    rs.getString("account_type"),
                    rs.getInt("balance"));
        }
        System.out.println(line);
    }
    //method to search customer
    public void searchCustomer() throws SQLException{
        System.out.print("Enter the Account No : ");
        int accountno = sc.nextInt();
        String query = "SELECT users.*,accounts.account_no,accounts.account_type,accounts.balance FROM users LEFT JOIN accounts ON users.user_id = accounts.user_id WHERE account_no = ?";
        PreparedStatement ps = con.prepareStatement(query);
        String format = "| %-8s | %-12s | %-20s | %-12s | %-12s | %-12s | %-10s |%n";
        String line = "+----------+--------------+----------------------+--------------+--------------+--------------+------------+";
        System.out.println(line);//table header
        System.out.printf(format, "User_id" , "Full_Name" ,"Email" , "Password" ,"Account No" ,"Type","Balance");
        System.out.println(line);
        ps.setInt(1,accountno);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            System.out.printf(format,rs.getInt("user_id"),
                    rs.getString("Full_Name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("account_no"),
                    rs.getString("account_type"),
                    rs.getInt("balance"));
        }
        else{
            System.out.println("User not found! Check Account No");
        }
        System.out.println(line);
    }
    //mehtod to delete customer
    public void deleteCustomer() throws SQLException{
        con.setAutoCommit(false);
        System.out.print("Enter the User ID You Want To Delete : ");
        int userid = sc.nextInt();
//        String query1 = "DELETE FROM accounts WHERE user_id = ?";
        String query = "DELETE FROM users WHERE user_id = ?";
        try{
            PreparedStatement ps = con.prepareStatement(query);
//            PreparedStatement psuser = con.prepareStatement(query2);
            ps.setInt(1,userid);
            ps.executeUpdate();
//            psuser.setInt(1,userid);
//            psuser.executeUpdate();
            con.commit();
            System.out.println("User Id" + userid + "and their Account Deleted Successfully");
        }catch(SQLException e){
            con.rollback();
            System.out.println("Deletion Failed");
            e.printStackTrace();
        }

    }
    //method to view all transactions
    public void viewTransactions() throws SQLException{
        String query = "SELECT * FROM transactions";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs =ps.executeQuery();
        String format = "| %-8s | %-20s | %-20s | %-12s | %-20s | %-20s |%n";
        String line = "+----------+----------------------+----------------------+--------------+----------------------+--------------------+";
        System.out.println(line);//table header
        System.out.printf(format, "Transaction_id" , "sender_accountNo" ,"receiver_accountNo" , "Transaction_Type" ,"Amount" ,"Date & Time");
        System.out.println(line);
        while(rs.next()){
            System.out.printf(format,rs.getInt("transaction_id"),
                    rs.getInt("sender_account"),
                    rs.getInt("receiver_account"),
                    rs.getString("transaction_type"),
                    rs.getInt("amount"),
                    rs.getTimestamp("transaction_date"));
        }
        System.out.println(line);
    }

}
