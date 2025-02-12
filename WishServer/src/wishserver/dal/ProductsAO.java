package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.Item;

public class ProductsAO {
    
    public static ArrayList<Item> getAllProducts() throws SQLException {
        ArrayList<Item> items = new ArrayList<>();
        DriverManager.registerDriver(new ClientDriver());
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            statement = con.prepareStatement("SELECT * FROM PRODUCTS");
            rs = statement.executeQuery();

            while (rs.next()) {
                items.add(new Item(rs.getInt("PRODUCT_ID"), rs.getString("NAME"), rs.getInt("PRICE")));
            }

            return items;
        } finally {
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (con != null) con.close();
        }
    }
}
