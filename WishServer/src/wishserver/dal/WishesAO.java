/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishserver.dal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.sql.Timestamp;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.Wish;
import wishserver.dto.User;

/**
 *
 * @author Otifi
 */
public class WishesAO {
        public static ArrayList<Wish> getAllWishes(String userName) throws SQLException{
            ArrayList<Wish> wishes = new ArrayList<>();
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            PreparedStatement statement = con.prepareStatement("SELECT * FROM Wish_View where OWNER_NAME = ? and (SUM_AMOUNT != PRICE or SUM_AMOUNT is null)");
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
               Wish tmp = new Wish(rs.getInt("PRODUCT_ID"), rs.getString("NAME"), rs.getInt("PRICE"), rs.getInt("SUM_AMOUNT"),rs.getInt("WISH_ID"));
               wishes.add(tmp);
            }
            return wishes;
        }
        
        public static boolean insertWish(String userName, int product_id) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("INSERT INTO Wish (owner_name, product_id, wish_datetime) VALUES (?, ?, ?)");
            statement.setString(1, userName);
            statement.setInt(2, product_id);
            Timestamp currentTimestamp = Timestamp.from(Instant.now());
            statement.setTimestamp(3, currentTimestamp);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
        
        public static boolean deleteWish(int wishID) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("delete from wish where wish_id = ?");
            statement.setInt(1, wishID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
        
        public static boolean wishContribute(String userName, int wishID, int amount) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("INSERT INTO CONTRIBUTION (WISH_ID, CONTRIBUTOR_NAME, CONTRIBUTION_DATETIME, AMOUNT) VALUES (?, ?, ?, ?)");
            statement.setInt(1, wishID);
            statement.setString(2, userName);
            Timestamp currentTimestamp = Timestamp.from(Instant.now());
            statement.setTimestamp(3, currentTimestamp);
            statement.setInt(4, amount);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
        
        public static ArrayList<User> getAllContributors(int wishID) throws SQLException{
            ArrayList<User> contributors = new ArrayList<>();
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            PreparedStatement statement = con.prepareStatement("select distinct CONTRIBUTOR_NAME from CONTRIBUTION where wish_id = ?");
            statement.setInt(1, wishID);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
               User tmp = new User(rs.getString("CONTRIBUTOR_NAME"));
               contributors.add(tmp);
            }
            return contributors;
        }
        
        public static boolean checkWish(int wishID) throws SQLException{
            
            return false;
        }
        
        
}
