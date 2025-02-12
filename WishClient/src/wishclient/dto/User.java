/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishclient.dto;

/**
 *
 * @author Otifi
 */
public class User {
    private String userName;
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

    public User(String userName, String fullName, int age, String gender, String phone) {
        this.userName = userName;
        this.fullName = fullName;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }
    
    public User(String userName) {
        this.userName = userName;
    }
    
    
}
