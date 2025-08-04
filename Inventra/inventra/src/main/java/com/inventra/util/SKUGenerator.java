package com.inventra.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import com.inventra.database.DBConnection;

public class SKUGenerator {
    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int SKU_LENGTH = 8;
    private static final Random random = new Random();

    // Generate base SKU string
    private static String generateCandidateSKU() {
        StringBuilder sku = new StringBuilder("SKU-");
        for (int i = 0; i < SKU_LENGTH - 4; i++) {
            sku.append(CHAR_POOL.charAt(random.nextInt(CHAR_POOL.length())));
        }
        return sku.toString();
    }

    // Check uniqueness and return final SKU
    public static String generateUniqueSKU() {
        try (Connection conn = DBConnection.getConnectionToDatabase()) {
            String sku;
            boolean exists;

            do {
                sku = generateCandidateSKU();
                exists = skuExists(conn, sku);
            } while (exists);

            return sku;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Database check
    private static boolean skuExists(Connection connection, String sku) {
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT COUNT(*) FROM products WHERE sku = ?")) {
            stmt.setString(1, sku);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
