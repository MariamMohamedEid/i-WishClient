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
 * @author LENOVO
 */
public class NotificationsAO {
    public static ArrayList<Notification> getAllNotifications(String userName) throws SQLException{
         ArrayList<Notification> notifications = new ArrayList<>();
         DriverManager.registerDriver(new ClientDriver());
         Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
         PreparedStatement statment = con.prepareStatement("SELECT * FROM NOTIFICATION"); 
         ResultSet rs = statment.executeQuery();
         while(rs.next()){
            Notification tmp = new Notification(rs.getString("Context"));
            notifications.add(tmp);
         }
         return notifications;
     }
    
}
