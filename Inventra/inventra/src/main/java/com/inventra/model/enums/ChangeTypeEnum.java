package com.inventra.model.enums;

// enum class. Accessed like: ChangeTypeEnum.ADDED
public enum ChangeTypeEnum {
    ADDED("Added"),
    MODIFIED("Modified"),
    REMOVED("Removed");

    private final String description;

    ChangeTypeEnum(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}