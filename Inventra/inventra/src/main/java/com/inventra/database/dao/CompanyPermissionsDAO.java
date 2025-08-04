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

}
