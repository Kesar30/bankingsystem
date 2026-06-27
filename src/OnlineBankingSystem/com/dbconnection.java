package OnlineBankingSystem.com;
import java.sql.*;

public class dbconnection {
    //connection variables created
    private static final String url = "jdbc:mysql://127.0.0.1:3306/bankingsystem";
    private static final String name = "root";
    private static final String password = "mysqlpassword@123";
    //creating method to return connection
    public Connection createconnection()throws SQLException{
       return DriverManager.getConnection(url,name,password);
    }
}