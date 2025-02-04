package wishserver.dto;

/**
 *
 * @author Otifi
 */
public class NewUser {
    private String userName;
    private String password;
    private String fullName;
    private int age;
    private String gender;
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public NewUser(String userName, String password, String fullName, int age, String gender, String phone) {
        this.userName = userName;
        this.password = password;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }
}

