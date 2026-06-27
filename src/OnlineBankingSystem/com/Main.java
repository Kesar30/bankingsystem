package OnlineBankingSystem.com;
import java.sql.Connection;

public class Main{
    public static void main (String[] args){
        try{
            //loading drivers only once
            Class.forName("com.mysql.cj.jdbc.Driver");
            //creating dbconnection class object for retuning connection
            dbconnection db = new dbconnection();
            Connection con = db.createconnection();
            //creating user class object and passing connection object in constructor and calling function
            user USER = new user(con);
            USER.registeruser();
            //releasing used resources
            con.close();
            //handling any exception if occurred
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}