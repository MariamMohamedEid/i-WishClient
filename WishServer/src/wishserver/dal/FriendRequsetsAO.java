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
import java.sql.Timestamp;
import java.time.Instant;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.User;

/**
 *
 * @author otifi
 */
//
public class FriendRequsetsAO {
    
            public static boolean insertFriendReq(String user1 ,String user2) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("insert into friendrequest (SENDER_NAME, RECEIVER_NAME, request_datetime) values (?, ?, ?)");
            statement.setString(1, user1);
            statement.setString(2, user2);
            Timestamp currentTimestamp = Timestamp.from(Instant.now());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
            
        public static boolean checkNonFriend(String user1 ,String user2) throws SQLException{
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            PreparedStatement statment = con.prepareStatement("SELECT * FROM FRIENDs\n" +
            "WHERE (USER_NAME = "+ user1 +" AND FRIEND_NAME = "+ user2 +")\n" +
            "OR (USER_NAME = "+ user2 +" AND FRIEND_NAME = "+ user1 +")"); 
            ResultSet rs = statment.executeQuery();
            if(rs.next())
                return false;
            else 
                return true;
        }
    
    
    
    
}
