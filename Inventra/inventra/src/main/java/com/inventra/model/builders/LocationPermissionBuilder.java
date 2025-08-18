package com.inventra.model.builders;

import com.inventra.model.beans.LocationPermission;

public class LocationPermissionBuilder {
    private final LocationPermission locationPermission;

    public LocationPermissionBuilder() {
        locationPermission = new LocationPermission();
    }

    public LocationPermissionBuilder withCompanyId(int companyId) {
        locationPermission.setCompanyId(companyId);
        return this;
    }

    public LocationPermissionBuilder withUserId(int userId) {
        locationPermission.setUserId(userId);
        return this;
    }

    public LocationPermissionBuilder withLocationId(int locationId) {
        locationPermission.setLocationId(locationId);
        return this;
    }

    public LocationPermissionBuilder withHierarchy(int hierarchy) {
        locationPermission.setHierarchy(hierarchy);
        return this;
    }

    public LocationPermissionBuilder withViewAudit(int viewAudit) {
        locationPermission.setViewAudit(viewAudit);
        return this;
    }

    public LocationPermissionBuilder withAddUser(int addUser) {
        locationPermission.setAddUser(addUser);
        return this;
    }

    public LocationPermissionBuilder withRemoveUser(int removeUser) {
        locationPermission.setRemoveUser(removeUser);
        return this;
    }

    public LocationPermissionBuilder withManageUserLocationPerm(int manageUserLocationPerm) {
        locationPermission.setManageUserLocationPerm(manageUserLocationPerm);
        return this;
    }

    public LocationPermissionBuilder withChangeName(int changeName) {
        locationPermission.setChangeName(changeName);
        return this;
    }

    public LocationPermissionBuilder withChangeAddress(int changeAddress) {
        locationPermission.setChangeAddress(changeAddress);
        return this;
    }

    public LocationPermissionBuilder withViewStock(int viewStock) {
        locationPermission.setViewStock(viewStock);
        return this;
    }

    public LocationPermissionBuilder withManageStock(int manageStock) {
        locationPermission.setManageStock(manageStock);
        return this;
    }

    public LocationPermission build() {
        return locationPermission;
    }

}
