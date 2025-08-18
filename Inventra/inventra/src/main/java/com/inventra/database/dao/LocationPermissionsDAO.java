package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.LocationPermission;

import java.sql.*;

public class LocationPermissionsDAO {

    public boolean insertLocationPermission(LocationPermission locationPermissions) throws SQLException {
        String query = "INSERT INTO location_permissions (user_id, location_id, company_id, hierarchy, view_audit, add_user, remove_user, manage_user_location_permissions, change_name, change_address, view_stock, manage_stock) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, locationPermissions.getUserId());
            stmt.setInt(2, locationPermissions.getLocationId());
            stmt.setInt(3, locationPermissions.getCompanyId());
            stmt.setInt(4, locationPermissions.getHierarchy());
            stmt.setInt(5, locationPermissions.getViewAudit());
            stmt.setInt(6, locationPermissions.getAddUser());
            stmt.setInt(7, locationPermissions.getRemoveUser());
            stmt.setInt(8, locationPermissions.getManageUserLocationPerm());
            stmt.setInt(9, locationPermissions.getChangeName());
            stmt.setInt(10, locationPermissions.getChangeAddress());
            stmt.setInt(11, locationPermissions.getViewStock());
            stmt.setInt(12, locationPermissions.getManageStock());
            return stmt.executeUpdate() > 0;
        }
    }

    public LocationPermission getLocationPermissionsByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM location_permissions WHERE user_id = ?";
        LocationPermission locationPermissions = null;
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    locationPermissions = new LocationPermission();
                    locationPermissions.setUserId(rs.getInt("user_id"));
                    locationPermissions.setLocationId(rs.getInt("location_id"));
                    locationPermissions.setCompanyId(rs.getInt("company_id"));
                    locationPermissions.setHierarchy(rs.getInt("hierarchy"));
                    locationPermissions.setViewAudit(rs.getInt("view_audit"));
                    locationPermissions.setAddUser(rs.getInt("add_user"));
                    locationPermissions.setRemoveUser(rs.getInt("remove_user"));
                    locationPermissions.setManageUserLocationPerm(rs.getInt("manage_user_location_permissions"));
                    locationPermissions.setChangeName(rs.getInt("change_name"));
                    locationPermissions.setChangeAddress(rs.getInt("change_address"));
                    locationPermissions.setViewStock(rs.getInt("view_stock"));
                    locationPermissions.setManageStock(rs.getInt("manage_stock"));
                }
            }
        }
        return locationPermissions;
    }

    public int getLocationIdByUserId(int userId) throws SQLException {
        String query = "SELECT location_id FROM location_permissions WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("location_id");
                }
            }
        }
        return -1;
    }

    public boolean updateLocationPermission(LocationPermission locationPermissions) throws SQLException {
        String query = "UPDATE location_permissions SET " +
                "hierarchy = ?, view_audit = ?, add_user = ?, remove_user = ?, manage_user_location_permissions = ?, change_name = ?, change_address = ?, view_stock = ?, manage_stock = ?, location_id = ? "
                +
                "WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, locationPermissions.getHierarchy());
            stmt.setInt(2, locationPermissions.getViewAudit());
            stmt.setInt(3, locationPermissions.getAddUser());
            stmt.setInt(4, locationPermissions.getRemoveUser());
            stmt.setInt(5, locationPermissions.getManageUserLocationPerm());
            stmt.setInt(6, locationPermissions.getChangeName());
            stmt.setInt(7, locationPermissions.getChangeAddress());
            stmt.setInt(8, locationPermissions.getViewStock());
            stmt.setInt(9, locationPermissions.getManageStock());
            stmt.setInt(10, locationPermissions.getLocationId());
            stmt.setInt(11, locationPermissions.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }
}
