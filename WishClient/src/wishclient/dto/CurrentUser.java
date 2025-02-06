/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wishclient.dto;

/**
 *
 * @author LENOVO
 */
public class CurrentUser extends User {
    private int points;

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public CurrentUser(String userName, int points) {
        super(userName);
        this.points = points;
    }
    
}
