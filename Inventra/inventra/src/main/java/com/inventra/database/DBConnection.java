package com.inventra.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnection {

    private static final String USERNAME = "root";
    private static final String PASSWORD = "Appraisal5201"; // EDIT ROOT PASSWORD
    private static final String CONN_STRING = "jdbc:mysql://localhost:3306/inventradatabase";

    public static Connection getConnectionToDatabase() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("[Inventra]: JDBC Driver Found!");
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            System.out.println("[Inventra]: Error: No JDBC Driver Found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("[Inventra]: Error: Connection Failed!");
            e.printStackTrace();
        }
        if (conn != null) {
            System.out.println("[Inventra]: Connection Successful!");
        }
        return conn;
    }

    // Create database and Users table
    public static void createDatabase() {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS inventradatabase;");
            // statement.executeUpdate("USE inventradatabase;");
            // start fresh every startup (remove if needed/wanting to save logs to the
            // server)
            /*
             * statement.executeUpdate("SET foreign_key_checks = 0;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `users`;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `companies`;");
             * statement.executeUpdate(DROP TABLE IF EXISTS `employees`;)
             * statement.executeUpdate("DROP TABLE IF EXISTS `locations`;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `company_permissions`;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `location_permissions`;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `products`;");
             * statement.executeUpdate("DROP TABLE IF EXISTS `inventories`;");
             * statement.executeUpdate(DROP TABLE IF EXISTS `audit`;)
             * statement.executeUpdate("SET foreign_key_checks = 1;");
             */

            // Create 'users' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `users` (" +
                    "`user_id` INT(11) AUTO_INCREMENT NOT NULL, " +
                    "`first_name` VARCHAR(25) NOT NULL, " +
                    "`last_name` VARCHAR(35) NOT NULL, " +
                    "`email` VARCHAR(100) NOT NULL, " +
                    "`password` VARCHAR(300) NOT NULL, " +
                    "`date_created` DATETIME DEFAULT NULL, " +
                    "`date_modified` DATETIME DEFAULT NULL, " +
                    "`verification_token` VARCHAR(255) DEFAULT NULL, " +
                    "`verified` BOOLEAN DEFAULT FALSE, " +
                    "`salt` VARCHAR(255) DEFAULT NULL, " +
                    "PRIMARY KEY (`user_id`));");

            // Create 'companies' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `companies` (" +
                    "`company_id` INT(11) AUTO_INCREMENT NOT NULL," +
                    "`name` VARCHAR(100) NOT NULL, " +
                    "`admin_id` INT NOT NULL, " +
                    "`date_created` DATETIME DEFAULT NULL, " +
                    "PRIMARY KEY (`company_id`), " +
                    "FOREIGN KEY (`admin_id`) REFERENCES users(user_id));");

            // Create 'employees' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `employees` (" +
                    "`user_id` INT(11) NOT NULL, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "PRIMARY KEY (`user_id`, `company_id`), " +
                    "FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES `companies`(`company_id`));");
            
            // Create 'locations' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `locations` (" +
                    "`location_id` INT(11) NOT NULL AUTO_INCREMENT, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "`name` VARCHAR(100) NOT NULL, " +
                    "`address_1` VARCHAR(100) DEFAULT NULL, " +
                    "`address_2` VARCHAR(100) DEFAULT NULL, " +
                    "PRIMARY KEY (`location_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES companies(company_id));");

            // Create 'company_permissions' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `company_permissions` (" +
                    "`user_id` INT(11) NOT NULL, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "`hierarchy` INT(3) NOT NULL DEFAULT '999', " +
                    "`view_audit` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`add/remove_user` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`manage_user_company_permissions` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`change_name` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`add/remove_product` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`edit_product` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "PRIMARY KEY (`user_id`, `company_id`), " +
                    "FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES `companies`(`company_id`));");

            // Create 'location_permissions' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `location_permissions` (" +
                    "`user_id` INT(11) NOT NULL, " +
                    "`location_id` INT(11) NOT NULL, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "`hierarchy` INT(3) NOT NULL  DEFAULT '999', " +
                    "`view_audit` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`add_user` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`remove_user` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`manage_user_location_permissions` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`change_name` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`change_address` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`view_stock` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "`manage_stock` TINYINT(1) NOT NULL DEFAULT '0', " +
                    "PRIMARY KEY (`user_id`, `location_id`), " +
                    "FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`), " +
                    "FOREIGN KEY (`location_id`) REFERENCES `locations`(`location_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES `companies`(`company_id`));");

            // Create 'products' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `products` (" +
                    "`product_id` INT(11) NOT NULL AUTO_INCREMENT, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "`name` VARCHAR(100) NOT NULL, " +
                    "`sku` VARCHAR(12), " +
                    "`description` VARCHAR(300), " +
                    "`price` DECIMAL(11,2), " +
                    "PRIMARY KEY (`product_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES `companies`(`company_id`));");

            // Create 'inventories' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `inventories` (" +
                    "`location_id` INT(11) NOT NULL, " +
                    "`product_id` INT(11) NOT NULL, " +
                    "`amount` INT(11) NOT NULL, " +
                    "FOREIGN KEY (`location_id`) REFERENCES `locations`(`location_id`), " +
                    "FOREIGN KEY (`product_id`) REFERENCES `products`(`product_id`));");

            // Create 'audit' table
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `audit` (" +
                    "`audit_id` INT(11) NOT NULL AUTO_INCREMENT, " +
                    "`company_id` INT(11) NOT NULL, " +
                    "`location_id` int(11), " +
                    "`user_id` INT(11) NOT NULL, " +
                    "`change_type` ENUM('ADDED','REMOVED','MODIFIED') NOT NULL, " +
                    "`change_target` ENUM('USER','COMPANY','PERMISSION','PRODUCT','LOCATION','INVENTORY') NOT NULL, " +
                    "`change_target_id` INT(11) NOT NULL, " +
                    "`change_column` VARCHAR(100) NOT NULL, " +
                    "`previous_value` VARCHAR(200) NOT NULL, " +
                    "`new_value` VARCHAR(200) NOT NULL, " +
                    "`datetime` DATETIME NOT NULL, " +
                    "PRIMARY KEY (`audit_id`), " +
                    "FOREIGN KEY (`company_id`) REFERENCES `companies`(`company_id`), " +
                    "FOREIGN KEY (`user_id`) REFERNECES `users`(`user_id`));");

            // Insert Admin Account to sign into for testing
            statement.executeUpdate(
                    "INSERT INTO users VALUES ('1', 'Admin', 'Inventra', 'admin@inventra.com', 'Admin123$%^', NOW(), NULL, NULL, TRUE);");
        } catch (SQLException e) {
            System.out.println("[SQL ERROR] State: " + e.getSQLState());
            System.out.println("[SQL ERROR] Error Code: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
