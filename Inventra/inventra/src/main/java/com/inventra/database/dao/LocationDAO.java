package com.inventra.database.dao;

import com.inventra.model.beans.Location;
import com.inventra.model.builders.LocationBuilder;
import com.inventra.database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO {

    public boolean insertLocation(Location location) throws SQLException {
        String sql = "INSERT INTO locations (company_id, name, address_1, address_2) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, location.getCompanyId());
            stmt.setString(2, location.getName());
            stmt.setString(3, location.getAddress1());
            stmt.setString(4, location.getAddress2());
            return stmt.executeUpdate() > 0;
        }
    }

    public int insertLocationAndReturnId(Location location) throws SQLException {
        String sql = "INSERT INTO locations (company_id, name, address_1, address_2) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, location.getCompanyId());
            stmt.setString(2, location.getName());
            stmt.setString(3, location.getAddress1());
            stmt.setString(4, location.getAddress2());
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
            return -1;
        }
    }

    public boolean updateLocation(Location location) throws SQLException {
        String query = "UPDATE locations SET name = ?, address_1 = ?, address_2 = ? WHERE location_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location.getName());
            stmt.setString(2, location.getAddress1());
            stmt.setString(3, location.getAddress2());
            stmt.setInt(4, location.getLocationId());
            return stmt.executeUpdate() > 0;
        }
    }

    public Location getLocationById(int id) throws SQLException {
        String sql = "SELECT * FROM locations WHERE location_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new LocationBuilder()
                            .withLocationId(rs.getInt("location_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withName(rs.getString("name"))
                            .withAddress1(rs.getString("address_1"))
                            .withAddress2(rs.getString("address_2"))
                            .build();
                }
            }
        }
        return null;
    }

    public List<Location> getLocationsByCompanyId(int companyId) throws SQLException {
        String sql = "SELECT * FROM locations WHERE company_id = ?";
        List<Location> locations = new ArrayList<>();

        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Location location = new LocationBuilder()
                            .withLocationId(rs.getInt("location_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withName(rs.getString("name"))
                            .withAddress1(rs.getString("address_1"))
                            .withAddress2(rs.getString("address_2"))
                            .build();
                    locations.add(location);
                }
            }
        }
        return locations;
    }

    public boolean deleteLocation(int locationId) throws SQLException {
        String sql = "DELETE FROM locations WHERE location_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            return stmt.executeUpdate() > 0;
        }
    }
}
