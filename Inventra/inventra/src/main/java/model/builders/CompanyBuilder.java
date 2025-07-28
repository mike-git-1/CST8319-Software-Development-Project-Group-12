package model.builders;
import java.util.Date;
import model.beans.Company;

public class CompanyBuilder {
    private final Company company;

    public CompanyBuilder() {
        company = new Company();
    }

    public CompanyBuilder withCompanyId(int companyId) {
        company.setCompanyId(companyId);
        return this;
    }

    public CompanyBuilder withName(String name) {
        company.setName(name);
        return this;
    }

    public CompanyBuilder withAdminId(int adminId) {
        company.setAdminId(adminId);
        return this;
    }

    public CompanyBuilder withDateCreated(Date dateCreated) {
        company.setDateCreated(dateCreated);
        return this;
    }

    public Company build() {
        return company;
    }
}
