package OnlineBankingSystem.com;
import java.util.Scanner;
import java.sql.*;

public class user {
//    creating instance variable
    private Connection con;
    //creating constructor that accepts connection object
    public user(Connection con ){
        this.con = con;
    }
    Scanner sc = new Scanner(System.in);

    //creating method to insert user data and storing in database
    public void registeruser() throws SQLException{


        //creating insert query
        String query = "INSERT INTO users(Full_Name,email,password) VALUES(?,?,?)";
//        passing into prepared statement
        PreparedStatement ps = con.prepareStatement(query);
        //defining loop to enter data
        while(true){
            //taking user input
            System.out.println("Enter Your Name : ");
            String Full_name = sc.nextLine();
            System.out.println("Enter Your Email : ");
            String Email = sc.nextLine();
            System.out.println("Enter Your Password upto 8 character : ");
            String password = sc.nextLine();
            //passing into database
            ps.setString(1,Full_name);
            ps.setString(2,Email);
            ps.setString(3,password);
            //adding multiple user data into a batch
            ps.addBatch();
            //asking user choice for more data
            System.out.println("Do You Want to Enter More user(Y/N) :");
            String choice = sc.nextLine();
            if (choice.equalsIgnoreCase("N")) {
                break;
            }

        }
        //executing batch data
        ps.executeBatch();
        System.out.println("Account Created Succesfully");
    }
    public void loginuser() throws SQLException {
        System.out.println("Enter your Email: ");
        String E_mail = sc.next();
        System.out.println("Enter your Password: ");
        String pass_word = sc.next();

        String query = "SELECT user_id,Full_Name FROM users WHERE email=? AND password=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, E_mail);
        ps.setString(2, pass_word);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            System.out.println("Welcome " + rs.getString("Full_Name"));
            System.out.println("Your user id is : " + rs.getInt("user_id"));
        } else {
            System.out.println("User not found or wrong password!");
        }
    }


}


