package wishserver.dto;

public class Item {
    private int product_ID;
    private String name;
    private int price;

    public Item(int product_ID, String name, int price) {
        this.product_ID = product_ID;
        this.name = name;
        this.price = price;
    }



    public int getProduct_ID() {
        return product_ID;
    }

    public void setProduct_ID(int product_ID) {
        this.product_ID = product_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }




    
}
