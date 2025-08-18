package com.inventra.model.beans;

public class Employee {

    private int companyId;
    private int userId;

    public Employee(int companyId, int userId) {
        this.companyId = companyId;
        this.userId = userId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}