package com.inventra.model.beans;

public class LocationPermission {
    private int userId;
    private int locationId;
    private int companyId;
    private int hierarchy;
    private int viewAudit;
    private int addUser;
    private int removeUser;
    private int manageUserLocationPerm;
    private int changeName;
    private int changeAddress;
    private int viewStock;
    private int manageStock;

    public LocationPermission() {
    }

    public LocationPermission(int userId, int locationId, int companyId, int hierarchy, int viewAudit, int addUser,
            int removeUser, int manageUserLocationPerm, int changeName, int changeAddress, int viewStock,
            int manageStock) {
        this.userId = userId;
        this.locationId = locationId;
        this.companyId = companyId;
        this.hierarchy = hierarchy;
        this.viewAudit = viewAudit;
        this.addUser = addUser;
        this.removeUser = removeUser;
        this.manageUserLocationPerm = manageUserLocationPerm;
        this.changeName = changeName;
        this.changeAddress = changeAddress;
        this.viewStock = viewStock;
        this.manageStock = manageStock;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getHierarchy() {
        return hierarchy;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public int getViewAudit() {
        return viewAudit;
    }

    public void setViewAudit(int viewAudit) {
        this.viewAudit = viewAudit;
    }

    public int getAddUser() {
        return addUser;
    }

    public void setAddUser(int addUser) {
        this.addUser = addUser;
    }

    public int getRemoveUser() {
        return removeUser;
    }

    public void setRemoveUser(int removeUser) {
        this.removeUser = removeUser;
    }

    public int getManageUserLocationPerm() {
        return manageUserLocationPerm;
    }

    public void setManageUserLocationPerm(int manageUserLocationPerm) {
        this.manageUserLocationPerm = manageUserLocationPerm;
    }

    public int getChangeName() {
        return changeName;
    }

    public void setChangeName(int changeName) {
        this.changeName = changeName;
    }

    public int getChangeAddress() {
        return changeAddress;
    }

    public void setChangeAddress(int changeAddress) {
        this.changeAddress = changeAddress;
    }

    public int getViewStock() {
        return viewStock;
    }

    public void setViewStock(int viewStock) {
        this.viewStock = viewStock;
    }

    public int getManageStock() {
        return manageStock;
    }

    public void setManageStock(int manageStock) {
        this.manageStock = manageStock;
    }

}
