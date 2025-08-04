package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.Inventory;
import com.inventra.model.beans.ProductInventoryDTO;
import com.inventra.model.builders.InventoryBuilder;
import com.inventra.model.builders.ProductInventoryDTOBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryDAO {

    public boolean insertInventory(Inventory inventory) throws SQLException {
        String sql = "INSERT INTO inventories (location_id, product_id, amount) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inventory.getLocationId());
            stmt.setInt(2, inventory.getProductId());
            stmt.setInt(3, inventory.getAmount());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateInventoryAmount(int locationId, int productId, int amount) throws SQLException {
        String sql = "UPDATE inventories SET amount = ? WHERE location_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, amount);
            stmt.setInt(2, locationId);
            stmt.setInt(3, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteInventory(int locationId, int productId) throws SQLException {
        String sql = "DELETE FROM inventories WHERE location_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    public Inventory getInventoryByLocationAndProduct(int locationId, int productId) throws SQLException {
        String sql = "SELECT * FROM inventories WHERE location_id = ? AND product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new InventoryBuilder()
                        .withLocationId(rs.getInt("location_id"))
                        .withProductId(rs.getInt("product_id"))
                        .withAmount(rs.getInt("amount"))
                        .build();
            }
        }
        return null;
    }

    public List<Inventory> getInventoriesByLocation(int locationId) throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT * FROM inventories WHERE location_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, locationId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Inventory inventory = new InventoryBuilder()
                        .withLocationId(rs.getInt("location_id"))
                        .withProductId(rs.getInt("product_id"))
                        .withAmount(rs.getInt("amount"))
                        .build();
                inventories.add(inventory);
            }
        }
        return inventories;
    }

    public List<Inventory> getInventoriesByProduct(int productId) throws SQLException {
        List<Inventory> inventories = new ArrayList<>();
        String sql = "SELECT * FROM inventories WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Inventory inventory = new InventoryBuilder()
                        .withLocationId(rs.getInt("location_id"))
                        .withProductId(rs.getInt("product_id"))
                        .withAmount(rs.getInt("amount"))
                        .build();
                inventories.add(inventory);
            }
        }
        return inventories;
    }

    public List<ProductInventoryDTO> getProductInventoryDTOByCompany(int companyId) throws SQLException {
        List<ProductInventoryDTO> productInventories = new ArrayList<>();
        String sql = "SELECT * FROM inventories JOIN products ON inventories.product_id = products.product_id WHERE products.company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductInventoryDTO productInventoryDTO = new ProductInventoryDTOBuilder()
                        .withProductId(rs.getInt("product_id"))
                        .withLocationId(rs.getInt("location_id"))
                        .withSku(rs.getString("sku"))
                        .withName(rs.getString("name"))
                        .withPrice(rs.getDouble("price"))
                        .withQuantity(rs.getInt("amount"))
                        .build();

                productInventories.add(productInventoryDTO);
            }
        }
        return productInventories;
    }
}
