package com.inventra.model.beans;

public class ProductInventoryDTO {
    private int productId;
    private int locationId;
    private String sku;
    private String name;
    private double price;
    private int quantity;

    public ProductInventoryDTO() {
    }

    public ProductInventoryDTO(int productId, int locationId, String sku, String name, double price, int quantity) {
        this.productId = productId;
        this.locationId = locationId;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
