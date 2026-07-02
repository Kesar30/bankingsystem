package OnlineBankingSystem.com;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class password {
    private Connection con;
    password(Connection con){
        this.con = con;
    }
    Scanner sc = new Scanner(System.in);

    public void changePassword( int id , String password) throws SQLException{
        String query = "UPDATE users SET password = ? WHERE user_id = ?";
        PreparedStatement ps =con.prepareStatement(query);
        System.out.println("Enter the Current Password : ");
        String currentPwd = sc.next();
        System.out.println("Enter the New Password : ");
        String newPwd = sc.next();
        ps.setString(1,newPwd);
        ps.setInt(2,id);
        if(currentPwd.equals(password)){
            ps.executeUpdate();
            System.out.println("Your Password Changed");
        }
        else{
            System.out.println("Current Password does not Match \nPlease Enter Correct Password.");
        }


    }
}
