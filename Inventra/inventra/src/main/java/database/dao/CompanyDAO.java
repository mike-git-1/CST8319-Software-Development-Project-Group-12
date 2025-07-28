package database.dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import database.DBConnection;
import model.beans.Company;
import model.builders.CompanyBuilder;

public class CompanyDAO {

    public boolean insertCompany(Company company) throws SQLException {
        String query = "INSERT INTO companies (name, admin_id, date_created) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, company.getName());
            stmt.setInt(2, company.getAdminId());
            stmt.setTimestamp(3, new Timestamp(company.getDateCreated().getTime()));
            return stmt.executeUpdate() > 0;
        }
    }

    public Company getCompanyById(int companyId) throws SQLException {
        String query = "SELECT * FROM companies WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new CompanyBuilder()
                        .withCompanyId(rs.getInt("company_id"))
                        .withName(rs.getString("name"))
                        .withAdminId(rs.getInt("admin_id"))
                        .withDateCreated(rs.getTimestamp("date_created"))
                        .build();
            }
        }
        return null;
    }

    public boolean updateCompany(Company company) throws SQLException {
        String query = "UPDATE companies SET name = ?, admin_id = ? WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, company.getName());
            stmt.setInt(2, company.getAdminId());
            stmt.setInt(3, company.getCompanyId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteCompany(int companyId) throws SQLException {
        String query = "DELETE FROM companies WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Company> getAllCompanies() throws SQLException {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM companies";
        try (Connection conn = DBConnection.getConnectionToDatabase();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Company company = new CompanyBuilder()
                        .withCompanyId(rs.getInt("company_id"))
                        .withName(rs.getString("name"))
                        .withAdminId(rs.getInt("admin_id"))
                        .withDateCreated(rs.getTimestamp("date_created"))
                        .build();
                companies.add(company);
            }
        }
        return companies;
    }
}
