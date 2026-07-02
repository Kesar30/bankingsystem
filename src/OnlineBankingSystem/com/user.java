package OnlineBankingSystem.com;
import java.util.Scanner;
import java.sql.*;

public class user {
//    creating instance variable
    private Connection con;
    private CustomerMenu menuObj;
    //creating constructor that accepts connection object
    public user(Connection con, CustomerMenu menuObj){
        this.con = con;
        this.menuObj = menuObj;
    }
    Scanner sc = new Scanner(System.in);

    //creating method to insert user data and storing in database
    public void registeruser() throws SQLException{


        //creating insert query
        String query = "INSERT INTO users(Full_Name,email,password) VALUES(?,?,?)";
//        passing into prepared statement and getting generated user id
        PreparedStatement ps = con.prepareStatement(
                query,
                Statement.RETURN_GENERATED_KEYS
        );
        // getting info from user
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
        // calling method by passing arguments to check existing user
        if (checkExistsUser(Email)) {
            System.out.println("User Already Exists. Please Login.");
            return;
        }

        ps.executeUpdate();
        System.out.println("Register Successfully");

// Continue with account creation...

        //storing generated user id
        ResultSet rs = ps.getGeneratedKeys();
        int userId = 0;
        if(rs.next()){
            userId = rs.getInt(1);
        }
        //selecting account type from user
        System.out.println("******Account Type******");
        System.out.println("Saving");
        System.out.println("Current");
        System.out.println("Enter Account Type: ");
        String acc_type = sc.nextLine();
        //inserting user account data in the accounts table
        String query2 = "INSERT INTO accounts(user_id, account_type, balance) VALUES(?, ?, ?)";
        PreparedStatement ps2 = con.prepareStatement(query2);
        ps2.setInt(1,userId);
        ps2.setString(2,acc_type);
        ps2.setInt(3,0);
        ps2.executeUpdate();
        System.out.println("Account Created Successfully");
        System.out.println("Inserted account for user " + userId);
        System.out.println("==================================");
        getaccount(userId);
        System.out.println("==================================");
    }
    //method to get account no and balance of the user
    public void getaccount(int id) throws SQLException{
        String query = "SELECT account_no, balance FROM accounts WHERE user_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        //storing account no. into resultset and printing
        ResultSet rs = ps.executeQuery();
        //checking whether the data is available or not using next function
        if (rs.next()) {
            System.out.println("Your user id is : " + id);
            System.out.println("Your Account No. is : " + rs.getInt("account_no"));
            System.out.println("Your Current Balance. is : " + rs.getInt("balance"));
        }
    }
    // method to check if user already exists
    public boolean checkExistsUser(String email) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    //user login function
    public void loginuser() throws SQLException {
        //getting user input data
        System.out.println("Enter your Email: ");
        String E_mail = sc.next();
        System.out.println("Enter your Password: ");
        String pass_word = sc.next();
        //writing and passing query
        String query = "SELECT user_id,Full_Name FROM users WHERE email=? AND password=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, E_mail);
        ps.setString(2, pass_word);
        //storing into resultset after executing query
        ResultSet rs = ps.executeQuery();
        //checking whether the data is available or not using next function
        if (rs.next()) {
            int id = rs.getInt("user_id");
            System.out.println("Welcome " + rs.getString("Full_Name"));
            System.out.println("Your user id is : " + id);
            callmenu(id,pass_word);
        } else {
            System.out.println("User not found or wrong password!");
        }
    }
    //method to call customermenu
    public void callmenu(int id ,String password) throws SQLException {
        String query = "SELECT account_no FROM accounts WHERE user_id = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            int accnumber = rs.getInt("account_no");
            menuObj.showMenu(accnumber,password,id);
        } else {
            System.out.println("No bank account found for this user.");
        }
    }





}


