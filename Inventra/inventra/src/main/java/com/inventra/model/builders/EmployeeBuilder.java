package com.inventra.model.builders;

import com.inventra.model.beans.Employee;

public class EmployeeBuilder {
    
    private int companyId;
    private int userId;

    public EmployeeBuilder withCompanyId(int companyId) {
        this.companyId = companyId;
        return this;
    }

    public EmployeeBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public Employee build() {
        return new Employee(companyId, userId);
    }

}
