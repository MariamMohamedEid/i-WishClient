
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
        public static ArrayList<Item> getAllProducts() throws SQLException{
         ArrayList<Item> itmes = new ArrayList<>();
         DriverManager.registerDriver(new ClientDriver());
         Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
         PreparedStatement statment = con.prepareStatement("SELECT * FROM PRODUCTS"); 
         ResultSet rs = statment.executeQuery();
         while(rs.next()){
            Item tmp = new Item(rs.getInt("PRODUCT_ID"), rs.getString("NAME"), rs.getInt("PRICE"));
            itmes.add(tmp);
         }
         return itmes;
     }
}
