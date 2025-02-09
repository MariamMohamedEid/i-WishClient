/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.Notification;
import wishserver.dto.User;

/**
 *
 * @author LENOVO
 */
public class FriendsAO {
        public static boolean insertFriend(String user1 ,String user2) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("INSERT INTO Friends (USER_name, Friend_name) VALUES (?, ?)");

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
        
        //not done
        public static boolean removeFriend(User usern1 ,User user2) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("");

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
        
        //not done
        public static ArrayList<User> getAllFriends(String userName) throws SQLException{
            ArrayList<User> friends = new ArrayList<>();
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            PreparedStatement statment = con.prepareStatement(""); 
            ResultSet rs = statment.executeQuery();
            while(rs.next()){
               User tmp = new User(rs.getString("Friend_Name"));
               friends.add(tmp);
            }
            return friends;
        }
    
}
