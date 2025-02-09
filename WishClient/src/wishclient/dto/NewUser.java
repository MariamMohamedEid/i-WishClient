package wishclient.dto;

/**
 *
 * @author Otifi
 */
public class NewUser extends User {

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    
    // Sign up Constructor
    public NewUser(String userName, String password, String fullName, int age, String gender, String phone) {
        super(userName, fullName, age, gender, phone);
        this.password = password; 
    }
    
    // Log in Constructor
    public NewUser(String userName, String password) {
        super(userName);
        this.password = password; 
    }
    
}
