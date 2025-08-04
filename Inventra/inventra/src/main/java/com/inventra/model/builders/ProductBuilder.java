package com.inventra.model.builders;

import com.inventra.model.beans.Product;

public class ProductBuilder {
    private int productId;
    private int companyId;
    private String name;
    private String sku;
    private String description;
    private double price;

    public ProductBuilder withProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public ProductBuilder withCompanyId(int companyId) {
        this.companyId = companyId;
        return this;
    }

    public ProductBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ProductBuilder withSku(String sku) {
        this.sku = sku;
        return this;
    }

    public ProductBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public ProductBuilder withPrice(double price) {
        this.price = price;
        return this;
    }

    public Product build() {
        return new Product(productId, companyId, name, sku, description, price);
    }
}
