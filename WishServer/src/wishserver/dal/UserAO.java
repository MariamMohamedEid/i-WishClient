package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.CurrentUser;
import wishserver.dto.NewUser;

public class UserAO {    
    
    public static boolean checkUserExist(NewUser newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ?");
            statement.setString(1, newUser.getUserName());
            rs = statement.executeQuery();
            return rs.next();
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    public static boolean checkUserExist2(String newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ?");
            statement.setString(1, newUser);
            rs = statement.executeQuery();
            return rs.next();
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static boolean insertUser(NewUser newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement(
                "INSERT INTO USER_S (USER_NAME, PASSWORD, FULL_NAME, AGE, GENDER, PHONE) VALUES (?, ?, ?, ?, ?, ?)"
            );
            statement.setString(1, newUser.getUserName());
            statement.setString(2, newUser.getPassword());
            statement.setString(3, newUser.getFullName());
            statement.setInt(4, newUser.getAge());
            statement.setString(5, newUser.getGender());
            statement.setString(6, newUser.getPhone());

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static CurrentUser getUser(NewUser newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT * FROM User_s WHERE USER_NAME = ?");
            statement.setString(1, newUser.getUserName());
            rs = statement.executeQuery();
            
            if (rs.next()) {
                return new CurrentUser(rs.getString("USER_NAME"), rs.getInt("Points"));
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static boolean validateUser(NewUser newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ? AND PASSWORD = ?");
            statement.setString(1, newUser.getUserName());
            statement.setString(2, newUser.getPassword().trim());
            rs = statement.executeQuery();
            
            return rs.next();
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static boolean updateUserPoints(String userName, int points) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            String updateQuery = "UPDATE USER_S SET POINTS = POINTS + ? WHERE USER_NAME = ?";
            statement = con.prepareStatement(updateQuery);
            statement.setInt(1, points);
            statement.setString(2, userName);

            System.out.println("Executing query: " + updateQuery + " with userName: " + userName + " and points: " + points);
            
            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
    
    public static CurrentUser getCurrentUser(String userName) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT * FROM User_s WHERE USER_NAME = ?");
            statement.setString(1, userName);
            rs = statement.executeQuery();
            
            if (rs.next()) {
                return new CurrentUser(rs.getString("USER_NAME"), rs.getInt("Points"));
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
}
