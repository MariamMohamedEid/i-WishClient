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
public class Wish extends Item{
    private int remaining;
    private int wishID;

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
    
    public int getWishID() {
        return wishID;
    }

    public void setWishID(int wishID) {
        this.wishID = wishID;
    }
    
    public Wish(int product_ID, String name, int price, int remaining, int wishID) {
        super(product_ID, name, price);
        this.remaining = remaining;
        this.wishID = wishID;
    }  
}
