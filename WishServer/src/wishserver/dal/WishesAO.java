package wishserver.dal;

import java.sql.Connection;
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

public class WishesAO {

    public static ArrayList<Wish> getAllWishes(String userName) throws SQLException {
        ArrayList<Wish> wishes = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT * FROM Wish_View WHERE OWNER_NAME = ? AND (SUM_AMOUNT != PRICE OR SUM_AMOUNT IS NULL)");
            statement.setString(1, userName);
            rs = statement.executeQuery();

            while (rs.next()) {
                wishes.add(new Wish(rs.getInt("PRODUCT_ID"), rs.getString("NAME"), rs.getInt("PRICE"), rs.getInt("REMAINING_AMOUNT"), rs.getInt("WISH_ID")));
            }
            return wishes;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean insertWish(String userName, int product_id) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("INSERT INTO Wish (owner_name, product_id, wish_datetime) VALUES (?, ?, ?)");
            statement.setString(1, userName);
            statement.setInt(2, product_id);
            statement.setTimestamp(3, Timestamp.from(Instant.now()));

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean deleteWish(int wishID) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("DELETE FROM Wish WHERE wish_id = ?");
            statement.setInt(1, wishID);

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static boolean wishContribute(String userName, int wishID, int amount) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("INSERT INTO CONTRIBUTION (WISH_ID, CONTRIBUTOR_NAME, CONTRIBUTION_DATETIME, AMOUNT) VALUES (?, ?, ?, ?)");
            statement.setInt(1, wishID);
            statement.setString(2, userName);
            statement.setTimestamp(3, Timestamp.from(Instant.now()));
            statement.setInt(4, amount);

            return statement.executeUpdate() > 0;
        } finally {
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static ArrayList<User> getAllContributors(int wishID) throws SQLException {
        ArrayList<User> contributors = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT DISTINCT CONTRIBUTOR_NAME FROM CONTRIBUTION WHERE wish_id = ?");
            statement.setInt(1, wishID);
            rs = statement.executeQuery();

            while (rs.next()) {
                contributors.add(new User(rs.getString("CONTRIBUTOR_NAME")));
            }
            return contributors;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }

    public static Wish getWishByID(int wishID) throws SQLException {
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            statement = con.prepareStatement("SELECT distinct * FROM Wish_View WHERE WISH_ID = ?");
            statement.setInt(1, wishID);
            rs = statement.executeQuery();

            if (rs.next()) {
                return new Wish(
                    rs.getInt("PRODUCT_ID"),
                    rs.getString("NAME"),
                    rs.getInt("PRICE"),
                    rs.getInt("REMAINING_AMOUNT"),
                    rs.getInt("WISH_ID")
                );
            }
            return null;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
}
