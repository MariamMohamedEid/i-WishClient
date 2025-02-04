package wishclint.dto;

public class LogIn {
    private String userName;
    private String Password;

    public LogIn(String userName, String Password) {
        this.userName = userName;
        this.Password = Password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return Password;
    }
        
}
