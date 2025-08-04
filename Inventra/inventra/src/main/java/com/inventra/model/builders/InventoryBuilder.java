package com.inventra.model.builders;

import com.inventra.model.beans.Inventory;

public class InventoryBuilder {
    private int locationId;
    private int productId;
    private int amount;

    public InventoryBuilder withLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }

    public InventoryBuilder withProductId(int productId) {
        this.productId = productId;
        return this;
    }

    public InventoryBuilder withAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public Inventory build() {
        return new Inventory(locationId, productId, amount);
    }
}
