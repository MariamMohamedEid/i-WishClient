
package wishserver.dto;

public class Wish extends Item{
    private int remaining;

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
    
    public Wish(int product_ID, String name, int price, int remaining) {
        super(product_ID, name, price);
        this.remaining = remaining;
    }  
}
