package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.CurrentUser;
import wishserver.dto.NewUser;
import wishserver.dto.User;

public class UserAO {    
        
    public static boolean checkUserExist(NewUser newUser) throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
        PreparedStatement statement;
        statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ?");
        statement.setString(1, newUser.getUserName());
        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
        
    public static boolean insertUser(NewUser newUser) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
        PreparedStatement statement;
        statement = con.prepareStatement("INSERT INTO USER_S (USER_NAME, PASSWORD, FULL_NAME, AGE, GENDER, PHONE) VALUES (?, ?, ?, ?, ?, ?)");
        statement.setString(1, newUser.getUserName());
        statement.setString(2, newUser.getPassword());
        statement.setString(3, newUser.getFullName());
        statement.setInt(4, newUser.getAge());
        statement.setString(5, newUser.getGender());
        statement.setString(6, newUser.getPhone());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            return true;
        } else {
            return false; 
        }   
    }
    
    public static CurrentUser getUser(NewUser newUser) throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
        PreparedStatement statement;
        statement = con.prepareStatement("SELECT * FROM User_s WHERE USER_NAME = ?");
        statement.setString(1, newUser.getUserName());
        ResultSet rs = statement.executeQuery();
        rs.next();
        CurrentUser user = new CurrentUser(rs.getString("USER_NAME"), rs.getInt("Points"));
        return user;
    }
    
    public static boolean validateUser(NewUser newUser) throws SQLException{
        DriverManager.registerDriver(new ClientDriver());
        Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
        PreparedStatement statement;
        statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ? AND Password = ?");
        statement.setString(1, newUser.getUserName());
        statement.setString(2, newUser.getPassword().trim());

        ResultSet rs = statement.executeQuery();
        return rs.next();
    }
    
    public static boolean updateUserPoints(String userName, int points) throws SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        try {
            DriverManager.registerDriver(new ClientDriver());
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");

            String updateQuery = "UPDATE USER_S SET POINTS = POINTS + ? WHERE USER_NAME = ?";
            statement = con.prepareStatement(updateQuery);
            statement.setInt(1, points);
            statement.setString(2, userName);
            System.out.println("Executing query: " + updateQuery + " with userName: " + userName + " and points: " + points);

            int rowsUpdated = statement.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            throw e; // Re-throw the exception to handle it in the caller
        } finally {
            if (statement != null) 
                statement.close();
            if (con != null) 
                con.close();
        }
    }
}