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

/**
 *
 * @author Otifi
 */
public class NotificationsAO {
    public static ArrayList<Notification> getAllNotifications(String userName) throws SQLException{
        ArrayList<Notification> notifications = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
        try{
            PreparedStatement statement = con.prepareStatement("select * from notification where receiver_name = ? order by notification_ID desc");
            statement.setString(1, userName);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
               Notification tmp = new Notification(rs.getString("CONTEXT"));
               notifications.add(tmp);
            }
            if(!rs.next() && notifications.size() == 0)
               notifications.add(new Notification("No Notifications Available")); 
            return notifications;
        }
        finally {
            if (con != null) {
                try {
                    con.close(); 
                }catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
public static void createNotification(String notification) throws SQLException{

    }
    
}
