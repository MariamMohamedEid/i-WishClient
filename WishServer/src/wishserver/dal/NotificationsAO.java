package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.Notification;

public class NotificationsAO {
    
    public static ArrayList<Notification> getAllNotifications(String userName) throws SQLException {
        ArrayList<Notification> notifications = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT * FROM notification WHERE receiver_name = ? ORDER BY notification_ID DESC");
            statement.setString(1, userName);
            rs = statement.executeQuery();

            while (rs.next()) {
                notifications.add(new Notification(rs.getString("CONTEXT")));
            }

            if (notifications.isEmpty()) {
                notifications.add(new Notification("No Notifications Available"));
            }
            
            return notifications;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static void createNotification(String notification, String userName) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("INSERT INTO notification (CONTEXT, receiver_name) VALUES (?, ?)");
            statement.setString(1, notification);
            statement.setString(2, userName);
            statement.executeUpdate();
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
}
