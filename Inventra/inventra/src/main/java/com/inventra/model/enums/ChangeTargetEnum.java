package com.inventra.model.enums;

public enum ChangeTargetEnum {
    USER("User"),
    COMPANY("Company"),
    PERMISSION("Permission"),
    PRODUCT("Product"),
    LOCATION("Location"),
    INVENTORY("Inventory");

    private final String description;

    ChangeTargetEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}