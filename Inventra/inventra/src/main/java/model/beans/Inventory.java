package model.beans;

public class Inventory {
    private int locationId;
    private int productId;
    private int amount;

    public Inventory() {}

    public Inventory(int locationId, int productId, int amount) {
        this.locationId = locationId;
        this.productId = productId;
        this.amount = amount;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
