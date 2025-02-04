package wishserver.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.derby.jdbc.ClientDriver;
import wishserver.dto.NewUser;

public class SignUp {    
        
        public static boolean checkUserExist(NewUser newUser) throws SQLException{
            DriverManager.registerDriver(new ClientDriver());
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "WISHDB", "123");
            PreparedStatement statement;
            statement = con.prepareStatement("SELECT * FROM USER_S WHERE USER_NAME = ?");
            statement.setString(1, newUser.getUserName());
            ResultSet rs = statement.executeQuery();
            return rs.next();
        }
        
        public static boolean insertEmp(NewUser newUser) throws SQLException {
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
            }
            else{
               return false; 
            }   
        }
    }
    
    
    
    
 
  
