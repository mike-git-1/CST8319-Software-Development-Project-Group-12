package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.CompanyPermission;
import java.sql.*;

public class CompanyPermissionsDAO {

    public boolean insertCompanyPermission(CompanyPermission companyPermission) throws SQLException {
        String query = "INSERT INTO company_permissions (user_id, company_id, hierarchy, view_audit, `add/remove_user`, manage_user_company_permissions,change_name,`add/remove_product`,edit_product) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, companyPermission.getUserId());
            stmt.setInt(2, companyPermission.getCompanyId());
            stmt.setInt(3, companyPermission.getHierarchy());
            stmt.setInt(4, companyPermission.getViewAudit());
            stmt.setInt(5, companyPermission.getAddRemoveUser());
            stmt.setInt(6, companyPermission.getManageUserCompanyPerm());
            stmt.setInt(7, companyPermission.getChangeName());
            stmt.setInt(8, companyPermission.getAddRemoveProduct());
            stmt.setInt(9, companyPermission.getEditProduct());
            return stmt.executeUpdate() > 0;
        }
    }

    public CompanyPermission getCompanyPermissionByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM company_permissions WHERE user_id = ?";
        CompanyPermission companyPermission = null;
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    companyPermission = new CompanyPermission();
                    companyPermission.setUserId(rs.getInt("user_id"));
                    companyPermission.setCompanyId(rs.getInt("company_id"));
                    companyPermission.setHierarchy(rs.getInt("hierarchy"));
                    companyPermission.setViewAudit(rs.getInt("view_audit"));
                    companyPermission.setAddRemoveUser(rs.getInt("add/remove_user"));
                    companyPermission.setManageUserCompanyPerm(rs.getInt("manage_user_company_permissions"));
                    companyPermission.setChangeName(rs.getInt("change_name"));
                    companyPermission.setAddRemoveProduct(rs.getInt("add/remove_product"));
                    companyPermission.setEditProduct(rs.getInt("edit_product"));
                }
            }
        }
        return companyPermission;
    }

    public int getCompanyIdByUserId(int userId) throws SQLException {
        String query = "SELECT company_id FROM company_permissions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("company_id");
                }
            }
        }
        return -1;
    }

    public boolean updateCompanyPermission(CompanyPermission companyPermission) throws SQLException {
        String query = "UPDATE company_permissions SET hierarchy = ?, view_audit = ?, " +
                " `add/remove_user` = ?, manage_user_company_permissions = ?, change_name = ?, " +
                " `add/remove_product` = ?, edit_product = ? WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, companyPermission.getHierarchy());
            stmt.setInt(2, companyPermission.getViewAudit());
            stmt.setInt(3, companyPermission.getAddRemoveUser());
            stmt.setInt(4, companyPermission.getManageUserCompanyPerm());
            stmt.setInt(5, companyPermission.getChangeName());
            stmt.setInt(6, companyPermission.getAddRemoveProduct());
            stmt.setInt(7, companyPermission.getEditProduct());
            stmt.setInt(8, companyPermission.getUserId());
            return stmt.executeUpdate() > 0;
        }
    }
}
