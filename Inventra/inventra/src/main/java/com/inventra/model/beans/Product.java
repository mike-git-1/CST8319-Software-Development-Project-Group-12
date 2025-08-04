package com.inventra.model.beans;

public class Product {
    private int productId;
    private int companyId;
    private String name;
    private String sku;
    private String description;
    private double price;

    public Product() {
    }

    public Product(int productId, int companyId, String name, String sku, String description, double price) {
        this.productId = productId;
        this.companyId = companyId;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
