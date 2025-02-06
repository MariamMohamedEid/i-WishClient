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
import wishserver.dto.Wish;
import wishserver.dto.User;

/**
 *
 * @author LENOVO
 */
public class WishesAO {
        public static ArrayList<Wish> getAllWishes(String userName) throws SQLException{
            ArrayList<Wish> wishes = new ArrayList<>();
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WishDB", "123");
            PreparedStatement statment = con.prepareStatement("SELECT * FROM Wish_View"); 
            ResultSet rs = statment.executeQuery();
            while(rs.next()){
               Wish tmp = new Wish(rs.getInt("wish_ID"), rs.getString("Name"), rs.getInt("Price"), rs.getInt("remaining"));
               wishes.add(tmp);
            }
            return wishes;
        }
        
        public static boolean insertWish(User usern1) throws SQLException {
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("INSERT INTO Wish (USER_ID, Friend_ID) VALUES (?, ?)");

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                return true;
            }
            else{
               return false; 
            }   
        }
}
