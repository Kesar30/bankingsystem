package OnlineBankingSystem.com;
import java.util.Scanner;
import java.sql.*;

public class balance {
    private Connection con ;
    balance(Connection con){

        this.con = con;
    }
    Scanner sc = new Scanner(System.in);

    public void deposit(int account) throws SQLException{
//        con.setAutoCommit(false);
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";
        PreparedStatement ps = con.prepareStatement(query);
//        System.out.println("Enter the Account Number: : ");
//        int account = sc.nextInt();
        System.out.println("Enter the Amount you want to Deposit: ");
        int amount = sc.nextInt();
        ps.setInt(1,amount);
        ps.setInt(2,account);
        ps.executeUpdate();
        System.out.println("Amount Successfully Deposit");
        getbalance(account);
        storeTransaction(account,null,"DEPOSIT",amount);
    }

    public void withdraw(int account) throws SQLException{
//        con.setAutoCommit(false);
        String query = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";
        PreparedStatement ps = con.prepareStatement(query);
//        System.out.println("Enter the Account Number: ");
//        int account = sc.nextInt();
        System.out.println("Enter the Amount you want to Withdraw: ");
        int amount = sc.nextInt();
        ps.setInt(1,amount);
        ps.setInt(2,account);
        if(check(amount,account)){
            ps.executeUpdate();
            System.out.println("Amount Successfully withdraw");
            getbalance(account);
            storeTransaction(account,null,"WITHDRAW",amount);
        }
        else {
            System.out.println("InSufficient balance");
        }

    }
    public boolean check(int amount,int account) throws SQLException{
        String query = "SELECT balance FROM accounts WHERE account_no = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1,account);
        ResultSet rs = ps.executeQuery();
        int prevbalance = 0;
        if(rs.next()){
           prevbalance = rs.getInt("balance");
        }
        return(prevbalance > amount);
    }
    public void transaction(int senderAccount) throws SQLException {
        con.setAutoCommit(false);

        try {

            System.out.print("Enter Receiver Account No : ");
            Long receiverAccount = sc.nextLong();

            System.out.print("Enter Amount : ");
            int amount = sc.nextInt();

            // Check balance first
            if (!check(amount, senderAccount)) {
                System.out.println("Insufficient Balance");
                con.rollback();
                return;
            }
            // Deduct from sender
            String deduct = "UPDATE accounts SET balance = balance - ? WHERE account_no = ?";

            PreparedStatement ps1 = con.prepareStatement(deduct);
            ps1.setInt(1, amount);
            ps1.setInt(2, senderAccount);

            int rows1 = ps1.executeUpdate();

            // Add to receiver
            String add = "UPDATE accounts SET balance = balance + ? WHERE account_no = ?";

            PreparedStatement ps2 = con.prepareStatement(add);
            ps2.setInt(1, amount);
            ps2.setLong(2, receiverAccount);
            int rows2 = ps2.executeUpdate();
            if (rows1 == 1 && rows2 == 1) {
                con.commit();
                System.out.println("Transaction Successful");
                storeTransaction(senderAccount,receiverAccount,"TRANSFER",amount);
            } else {
                con.rollback();
                System.out.println("Transaction Failed");
            }

        }
        catch(Exception e){
            con.rollback();
            throw e;
        }
        finally{
            con.setAutoCommit(true);
        }
    }
    public void getbalance(int accountno) throws SQLException {
//        System.out.println("Enter the User Id: ");
//        int id = sc.nextInt();
        String query = "SELECT balance FROM accounts WHERE account_no = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, accountno);
        //storing account no. into resultset and printing
        ResultSet rs = ps.executeQuery();
        //checking whether the data is available or not using next function
        if (rs.next()) {
            System.out.println("Your Current Balance. is : ₹" + rs.getInt("balance"));
        }
    }
    public void storeTransaction(long sender, Long receiver, String type, double amount) throws SQLException {
//        System.out.println("Storing transaction");
        String query = "INSERT INTO transactions (sender_account, receiver_account, transaction_type, amount) VALUES (?, ?, ?, ?) ";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, sender);
        if (receiver == null) {
            ps.setNull(2, Types.BIGINT);
        } else {
            ps.setLong(2, receiver);
        }
        ps.setString(3, type);
        ps.setDouble(4, amount);
        ps.executeUpdate();

    }

    // method to storing transaction history into table
    public void getTransaction(int accountno) throws SQLException{
        String query = "SELECT * FROM transactions WHERE sender_account = ? OR receiver_account = ? ORDER BY transaction_date DESC;";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setLong(1, accountno);
        ps.setLong(2, accountno);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            long sender = rs.getLong("sender_account");
            long receiver = rs.getLong("receiver_account");
//            boolean receiverNull = rs.wasNull();
            String type = rs.getString("transaction_type");
            double amount = rs.getDouble("amount");
            Timestamp date = rs.getTimestamp("transaction_date");
            if(type.equals("DEPOSIT")) {
                System.out.println(date + " | Deposited ₹" + amount);
            }
            else if(type.equals("WITHDRAW")) {
                System.out.println(date + " | Withdrawn ₹" + amount);
            }
            else if(type.equals("TRANSFER")) {
                if(sender == accountno) {
                    System.out.println(date + " | Transferred ₹" + amount + " to Account " + receiver);
                } else {
                    System.out.println(date + " | Received ₹" + amount + " from Account " + sender);
                }
            }
        }
    }
    /*public void transaction_storing(int senderAccount,int receiverAccount,int amount) throws SQLException{
        // storing into transaction table
        String query = "INSERT INTO transactions (sender_account,receiver_account,transaction_type,amount) VALUES (?,?,?,?)";
        PreparedStatement ps3 = con.prepareStatement(query);
        ps3.setInt(1, senderAccount);
        ps3.setInt(2, receiverAccount);
        ps3.setString(3, "Transfer");
        ps3.setInt(4, amount);
        ps3.executeUpdate();
    }
    //method to storing debit history into table
    public void deposit_storing(int Account, int amount) throws SQLException{
        // storing into transaction table
        String query = "INSERT INTO transactions (sender_account,transaction_type,amount) VALUES (?,?,?)";
        PreparedStatement ps3 = con.prepareStatement(query);
        ps3.setInt(1, Account);
        ps3.setString(2, "Deposit");
        ps3.setInt(3, amount);
        ps3.executeUpdate();
    }
    public void withdraw_storing(int Account, int amount) throws SQLException{
        // storing into transaction table
        String query = "INSERT INTO transactions (sender_account,transaction_type,amount) VALUES (?,?,?)";
        PreparedStatement ps3 = con.prepareStatement(query);
        ps3.setInt(1, Account);
        ps3.setString(2, "Credited");
        ps3.setInt(3, amount);
        ps3.executeUpdate();
    }
    */

    }
