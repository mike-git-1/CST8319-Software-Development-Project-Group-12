package com.inventra.model.beans;

public class CompanyPermission {
    private int userId;
    private int companyId;
    private int hierarchy;
    private int viewAudit;
    private int addRemoveUser;
    private int manageUserCompanyPerm;
    private int changeName;
    private int addRemoveProduct;
    private int editProduct;

    public CompanyPermission() {
    }

    public CompanyPermission(int userId, int companyId, int hierarchy, int viewAudit, int addRemoveUser,
            int manageUserCompanyPerm, int changeName, int addRemoveProduct, int editProduct) {
        this.userId = userId;
        this.companyId = companyId;
        this.hierarchy = hierarchy;
        this.viewAudit = viewAudit;
        this.addRemoveUser = addRemoveUser;
        this.manageUserCompanyPerm = manageUserCompanyPerm;
        this.changeName = changeName;
        this.addRemoveProduct = addRemoveProduct;
        this.editProduct = editProduct;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getAddRemoveUser() {
        return addRemoveUser;
    }

    public void setAddRemoveUser(int addRemoveUser) {
        this.addRemoveUser = addRemoveUser;
    }

    public int getManageUserCompanyPerm() {
        return manageUserCompanyPerm;
    }

    public void setManageUserCompanyPerm(int manageUserCompanyPerm) {
        this.manageUserCompanyPerm = manageUserCompanyPerm;
    }

    public int getChangeName() {
        return changeName;
    }

    public void setChangeName(int changeName) {
        this.changeName = changeName;
    }

    public int getAddRemoveProduct() {
        return addRemoveProduct;
    }

    public void setAddRemoveProduct(int addRemoveProduct) {
        this.addRemoveProduct = addRemoveProduct;
    }

    public int getEditProduct() {
        return editProduct;
    }

    public void setEditProduct(int editProduct) {
        this.editProduct = editProduct;
    }
}
