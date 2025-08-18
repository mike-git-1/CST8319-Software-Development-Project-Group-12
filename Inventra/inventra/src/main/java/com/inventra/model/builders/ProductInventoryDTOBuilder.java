package com.inventra.model.builders;

import com.inventra.model.beans.ProductInventoryDTO;

public class ProductInventoryDTOBuilder {
    private int productId;
    private int locationId;
    private String location;
    private String sku;
    private String name;
    private double price;
    private int quantity;

    public ProductInventoryDTOBuilder withProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public ProductInventoryDTOBuilder withLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public ProductInventoryDTOBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public ProductInventoryDTOBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductInventoryDTOBuilder withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public ProductInventoryDTOBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public ProductInventoryDTOBuilder withQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public ProductInventoryDTO build() {
        return new ProductInventoryDTO(productId, locationId, location, sku, name, price, quantity);
    }
}
