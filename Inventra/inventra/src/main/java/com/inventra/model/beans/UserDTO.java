package com.inventra.model.beans;

import java.sql.Date;

public class UserDTO {

    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private Date date_created;
    private boolean verified;
    private String location;
    private int locationId;
    private int compViewAudit;
    private int compAddRemoveUser;
    private int compManageUserCompanyPerm;
    private int compChangeName;
    private int compAddRemoveProduct;
    private int compEditProduct;
    private int locViewAudit;
    private int locAddUser;
    private int locRemoveUser;
    private int locManageUserLocationPerm;
    private int locChangeName;
    private int locChangeAddress;
    private int locViewStock;
    private int locManageStock;

    public UserDTO() {
    }

    public UserDTO(int id, String first_name, String last_name, String email, Date date_created,
            boolean verified, String location, int locationId, int compViewAudit, int compAddRemoveUser,
            int compManageUserCompanyPerm,
            int compChangeName, int compAddRemoveProduct, int compEditProduct, int locViewAudit, int locAddUser,
            int locRemoveUser, int locManageUserLocationPerm, int locChangeName, int locChangeAddress, int locViewStock,
            int locManageStock) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.date_created = date_created;
        this.verified = verified;
        this.location = location;
        this.locationId = locationId;
        this.compViewAudit = compViewAudit;
        this.compAddRemoveUser = compAddRemoveUser;
        this.compManageUserCompanyPerm = compManageUserCompanyPerm;
        this.compChangeName = compChangeName;
        this.compAddRemoveProduct = compAddRemoveProduct;
        this.compEditProduct = compEditProduct;
        this.locViewAudit = locViewAudit;
        this.locAddUser = locAddUser;
        this.locRemoveUser = locRemoveUser;
        this.locManageUserLocationPerm = locManageUserLocationPerm;
        this.locChangeName = locChangeName;
        this.locChangeAddress = locChangeAddress;
        this.locViewStock = locViewStock;
        this.locManageStock = locManageStock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public int getCompViewAudit() {
        return compViewAudit;
    }

    public void setCompViewAudit(int compViewAudit) {
        this.compViewAudit = compViewAudit;
    }

    public int getCompAddRemoveUser() {
        return compAddRemoveUser;
    }

    public void setCompAddRemoveUser(int compAddRemoveUser) {
        this.compAddRemoveUser = compAddRemoveUser;
    }

    public int getCompManageUserCompanyPerm() {
        return compManageUserCompanyPerm;
    }

    public void setCompManageUserCompanyPerm(int compManageUserCompanyPerm) {
        this.compManageUserCompanyPerm = compManageUserCompanyPerm;
    }

    public int getCompChangeName() {
        return compChangeName;
    }

    public void setCompChangeName(int compChangeName) {
        this.compChangeName = compChangeName;
    }

    public int getCompAddRemoveProduct() {
        return compAddRemoveProduct;
    }

    public void setCompAddRemoveProduct(int compAddRemoveProduct) {
        this.compAddRemoveProduct = compAddRemoveProduct;
    }

    public int getCompEditProduct() {
        return compEditProduct;
    }

    public void setCompEditProduct(int compEditProduct) {
        this.compEditProduct = compEditProduct;
    }

    public int getLocViewAudit() {
        return locViewAudit;
    }

    public void setLocViewAudit(int locViewAudit) {
        this.locViewAudit = locViewAudit;
    }

    public int getLocAddUser() {
        return locAddUser;
    }

    public void setLocAddUser(int locAddUser) {
        this.locAddUser = locAddUser;
    }

    public int getLocRemoveUser() {
        return locRemoveUser;
    }

    public void setLocRemoveUser(int locRemoveUser) {
        this.locRemoveUser = locRemoveUser;
    }

    public int getLocManageUserLocationPerm() {
        return locManageUserLocationPerm;
    }

    public void setLocManageUserLocationPerm(int locManageUserLocationPerm) {
        this.locManageUserLocationPerm = locManageUserLocationPerm;
    }

    public int getLocChangeName() {
        return locChangeName;
    }

    public void setLocChangeName(int locChangeName) {
        this.locChangeName = locChangeName;
    }

    public int getLocChangeAddress() {
        return locChangeAddress;
    }

    public void setLocChangeAddress(int locChangeAddress) {
        this.locChangeAddress = locChangeAddress;
    }

    public int getLocViewStock() {
        return locViewStock;
    }

    public void setLocViewStock(int locViewStock) {
        this.locViewStock = locViewStock;
    }

    public int getLocManageStock() {
        return locManageStock;
    }

    public void setLocManageStock(int locManageStock) {
        this.locManageStock = locManageStock;
    }
}