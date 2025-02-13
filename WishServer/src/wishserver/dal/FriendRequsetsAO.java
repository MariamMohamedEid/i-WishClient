package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.User;

public class FriendRequsetsAO {
    
    public static boolean insertFriendReq(String user1, String user2) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement(
                "INSERT INTO friendRequest (SENDER_NAME, RECEIVER_NAME, request_datetime) VALUES (?, ?, ?)"
            );
            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setTimestamp(3, Timestamp.from(Instant.now()));

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean checkNonFriend(String user1, String user2) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement(
                "SELECT * FROM FRIENDS WHERE (USER_NAME = ? AND FRIEND_NAME = ?) OR (USER_NAME = ? AND FRIEND_NAME = ?)"
            );
            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setString(3, user2);
            statement.setString(4, user1);
            rs = statement.executeQuery();
            return !rs.next();
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean checkRequest(String user1, String user2) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement(
                "SELECT * FROM friendRequest WHERE SENDER_NAME = ? AND RECEIVER_NAME = ?"
            );
            statement.setString(1, user1);
            statement.setString(2, user2);
            rs = statement.executeQuery();
            return !rs.next();
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean deleteFriendReq(String user1, String user2) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement(
                "DELETE FROM friendRequest WHERE (SENDER_NAME = ? AND RECEIVER_NAME = ?) OR (SENDER_NAME = ? AND RECEIVER_NAME = ?)"
            );
            statement.setString(1, user1);
            statement.setString(2, user2);
            statement.setString(3, user2);
            statement.setString(4, user1);

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static ArrayList<User> getAllFriendRequests(String userName) throws SQLException {
        ArrayList<User> friends = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement(
                "SELECT sender_name FROM friendRequest WHERE RECEIVER_NAME = ?"
            );
            statement.setString(1, userName);
            rs = statement.executeQuery();

            while (rs.next()) {
                friends.add(new User(rs.getString("sender_name")));
            }
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
        return friends;
    }
}
