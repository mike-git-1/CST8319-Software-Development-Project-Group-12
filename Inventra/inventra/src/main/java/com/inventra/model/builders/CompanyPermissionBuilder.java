package com.inventra.model.builders;

import com.inventra.model.beans.CompanyPermission;

public class CompanyPermissionBuilder {
    private final CompanyPermission companyPermission;

    public CompanyPermissionBuilder() {
        companyPermission = new CompanyPermission();
    }

    public CompanyPermissionBuilder withCompanyId(int companyId) {
        companyPermission.setCompanyId(companyId);
        return this;
    }

    public CompanyPermissionBuilder withUserId(int userId) {
        companyPermission.setUserId(userId);
        return this;
    }

    public CompanyPermissionBuilder withHierarchy(int hierarchy) {
        companyPermission.setHierarchy(hierarchy);
        return this;
    }

    public CompanyPermissionBuilder withViewAudit(int viewAudit) {
        companyPermission.setViewAudit(viewAudit);
        return this;
    }

    public CompanyPermissionBuilder withAddRemoveUser(int addRemoveUser) {
        companyPermission.setAddRemoveUser(addRemoveUser);
        return this;
    }

    public CompanyPermissionBuilder withManageUserCompanyPerm(int manageUserCompanyPerm) {
        companyPermission.setAddRemoveUser(manageUserCompanyPerm);
        return this;
    }

    public CompanyPermissionBuilder withChangeName(int changeName) {
        companyPermission.setChangeName(changeName);
        return this;
    }

    public CompanyPermissionBuilder withAddRemoveProduct(int addRemoveProduct) {
        companyPermission.setAddRemoveProduct(addRemoveProduct);
        return this;
    }

    public CompanyPermissionBuilder withEditProduct(int editProduct) {
        companyPermission.setEditProduct(editProduct);
        return this;
    }

    public CompanyPermission build() {
        return companyPermission;
    }

}
