package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.Product;
import com.inventra.model.builders.ProductBuilder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public boolean insertProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (company_id, name, sku, description, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, product.getCompanyId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getSku());
            stmt.setString(4, product.getDescription());
            stmt.setDouble(5, product.getPrice());
            return stmt.executeUpdate() > 0;
        }
    }

    public int insertProductAndReturnId(Product product) throws SQLException {
        String query = "INSERT INTO products (company_id, name, sku, description, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, product.getCompanyId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getSku());
            stmt.setString(4, product.getDescription());
            stmt.setDouble(5, product.getPrice());
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

    public Product getProductById(int productId) throws SQLException {
        String query = "SELECT * FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new ProductBuilder()
                        .withProductId(rs.getInt("product_id"))
                        .withCompanyId(rs.getInt("company_id"))
                        .withName(rs.getString("name"))
                        .withSku(rs.getString("sku"))
                        .withDescription(rs.getString("description"))
                        .withPrice(rs.getDouble("price"))
                        .build();
            }
        }
        return null;
    }

    public List<Product> getProductsByCompanyId(int companyId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products WHERE company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, companyId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new ProductBuilder()
                        .withProductId(rs.getInt("product_id"))
                        .withCompanyId(rs.getInt("company_id"))
                        .withName(rs.getString("name"))
                        .withSku(rs.getString("sku"))
                        .withDescription(rs.getString("description"))
                        .withPrice(rs.getDouble("price"))
                        .build();
                products.add(product);
            }
        }
        System.out.println(products);
        return products;
    }

    public boolean updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET name = ?, sku = ?, description = ?, price = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getProductId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteProduct(int productId) throws SQLException {
        String query = "DELETE FROM products WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, productId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new ProductBuilder()
                        .withProductId(rs.getInt("product_id"))
                        .withCompanyId(rs.getInt("company_id"))
                        .withName(rs.getString("name"))
                        .withSku(rs.getString("sku"))
                        .withDescription(rs.getString("description"))
                        .withPrice(rs.getDouble("price"))
                        .build();
                products.add(product);
            }
        }
        return products;
    }
}
