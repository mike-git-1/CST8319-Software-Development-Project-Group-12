package com.inventra.database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.User;
import com.inventra.model.beans.UserDTO;
import com.inventra.model.builders.UserBuilder;
import com.inventra.model.builders.UserDTOBuilder;

public class UserDAO {

    private static final String INSERT_SQL = "INSERT INTO users " +
            "(first_name, last_name, email, password, date_created, verification_token, verified, salt) VALUES " +
            "(?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = "UPDATE users SET " +
            "first_name = ?, last_name = ?, email = ?, password = ?, date_modified = ?, salt = ? " +
            "WHERE user_id = ?;";

    private static final String SELECT_ALL_BY_ID_SQL = "SELECT * FROM users WHERE user_id = ?;";

    private static final String SELECT_ID_SQL = "SELECT user_id, first_name, last_name, email, verified FROM users WHERE user_id = ?;";

    private static final String SELECT_ALL_SQL = "SELECT * FROM users;";

    private static final String DELETE_SQL = "DELETE FROM users WHERE user_id = ?;";

    // Insert a new user with verification details
    public boolean insertUser(User user) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, user.getFirst_name());
            preparedStatement.setString(2, user.getLast_name());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
            preparedStatement.setString(6, user.getVerification_token());
            preparedStatement.setBoolean(7, user.isVerified());
            preparedStatement.setString(8, user.getSalt());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int insertUserAndReturnId(User user) throws SQLException {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL,
                        PreparedStatement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, user.getFirst_name());
            preparedStatement.setString(2, user.getLast_name());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
            preparedStatement.setString(6, user.getVerification_token());
            preparedStatement.setBoolean(7, user.isVerified());
            preparedStatement.setString(8, user.getSalt());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }

            return -1;
        }
    }

    // Verify user by token
    public boolean verifyUserByToken(String token) {
        String query = "UPDATE users SET verified = true, verification_token = NULL WHERE verification_token = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, token);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update an existing user
    public boolean updateUser(User user) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_SQL)) {

            preparedStatement.setString(1, user.getFirst_name());
            preparedStatement.setString(2, user.getLast_name());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
            preparedStatement.setString(6, user.getSalt());
            preparedStatement.setInt(7, user.getId());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Select user by ID
    public User selectUser(int id) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new UserBuilder()
                        .setID(resultSet.getInt("user_id"))
                        .setFirstName(resultSet.getString("first_name"))
                        .setLastName(resultSet.getString("last_name"))
                        .setEmail(resultSet.getString("email"))
                        .setVerified(resultSet.getBoolean("verified"))
                        .build();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User selectUserById(int id) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BY_ID_SQL)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return buildUserFromResultSet(resultSet);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<UserDTO> getUsersByCompanyId(int companyId) throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        String SQL_QUERY = "SELECT " +
                "users.user_id AS id, " +
                "users.first_name AS first_name, " +
                "users.last_name AS last_name, " +
                "users.email AS email, " +
                "users.date_created AS dateCreated, " +
                "users.verified AS verified, " +
                "locations.location_id AS locationId, " +
                "locations.name AS location, " +
                "`company_permissions`.`view_audit` AS compViewAudit, " +
                "`company_permissions`.`add/remove_user` AS compAddRemoveUser, " +
                "company_permissions.manage_user_company_permissions AS compManageUserCompanyPerm, " +
                "company_permissions.change_name AS compChangeName, " +
                "`company_permissions`.`add/remove_product` AS compAddRemoveProduct, " +
                "company_permissions.edit_product AS compEditProduct, " +
                "location_permissions.view_audit AS locViewAudit, " +
                "location_permissions.add_user AS locAddUser, " +
                "location_permissions.remove_user AS locRemoveUser, " +
                "location_permissions.manage_user_location_permissions AS locManageUserLocationPerm, " +
                "location_permissions.change_name AS locChangeName, " +
                "location_permissions.change_address AS locChangeAddress, " +
                "location_permissions.view_stock AS locViewStock, " +
                "location_permissions.manage_stock AS locManageStock " +
                "FROM users " +
                "INNER JOIN company_permissions ON users.user_id = company_permissions.user_id " +
                "LEFT JOIN location_permissions ON users.user_id = location_permissions.user_id " +
                "LEFT JOIN locations ON location_permissions.location_id = locations.location_id " +
                "WHERE company_permissions.company_id = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setInt(1, companyId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserDTO user = new UserDTOBuilder()
                        .withId(resultSet.getInt("id"))
                        .withFirstName(resultSet.getString("first_name"))
                        .withLastName(resultSet.getString("last_name"))
                        .withEmail(resultSet.getString("email"))
                        .withDateCreated(resultSet.getDate("dateCreated"))
                        .withVerified(resultSet.getBoolean("verified"))
                        .withLocationId(resultSet.getInt("locationId"))
                        .withLocation(resultSet.getString("location"))
                        .withCompViewAudit(resultSet.getInt("compViewAudit"))
                        .withCompAddRemoveUser(resultSet.getInt("compAddRemoveUser"))
                        .withCompManageUserCompanyPerm(resultSet.getInt("compManageUserCompanyPerm"))
                        .withCompChangeName(resultSet.getInt("compChangeName"))
                        .withCompAddRemoveProduct(resultSet.getInt("compAddRemoveProduct"))
                        .withCompEditProduct(resultSet.getInt("compEditProduct"))
                        .withLocViewAudit(resultSet.getInt("locViewAudit"))
                        .withLocAddUser(resultSet.getInt("locAddUser"))
                        .withLocRemoveUser(resultSet.getInt("locRemoveUser"))
                        .withLocManageUserLocationPerm(resultSet.getInt("locManageUserLocationPerm"))
                        .withLocChangeName(resultSet.getInt("locChangeName"))
                        .withLocChangeAddress(resultSet.getInt("locChangeAddress"))
                        .withLocViewStock(resultSet.getInt("locViewStock"))
                        .withLocManageStock(resultSet.getInt("locManageStock"))
                        .build();

                users.add(user);
            }
        }
        return users;

    }

    public List<UserDTO> getUsersByLocationId(int locationId) throws SQLException {
        List<UserDTO> users = new ArrayList<>();
        String SQL_QUERY = "SELECT " +
                "users.user_id AS id, " +
                "users.first_name AS first_name, " +
                "users.last_name AS last_name, " +
                "users.email AS email, " +
                "users.date_created AS dateCreated, " +
                "users.verified AS verified, " +
                "locations.location_id AS locationId, " +
                "locations.name AS location, " +
                "`company_permissions`.`view_audit` AS compViewAudit, " +
                "`company_permissions`.`add/remove_user` AS compAddRemoveUser, " +
                "company_permissions.manage_user_company_permissions AS compManageUserCompanyPerm, " +
                "company_permissions.change_name AS compChangeName, " +
                "`company_permissions`.`add/remove_product` AS compAddRemoveProduct, " +
                "company_permissions.edit_product AS compEditProduct, " +
                "location_permissions.view_audit AS locViewAudit, " +
                "location_permissions.add_user AS locAddUser, " +
                "location_permissions.remove_user AS locRemoveUser, " +
                "location_permissions.manage_user_location_permissions AS locManageUserLocationPerm, " +
                "location_permissions.change_name AS locChangeName, " +
                "location_permissions.change_address AS locChangeAddress, " +
                "location_permissions.view_stock AS locViewStock, " +
                "location_permissions.manage_stock AS locManageStock " +
                "FROM users " +
                "LEFT JOIN company_permissions ON users.user_id = company_permissions.user_id " +
                "INNER JOIN location_permissions ON users.user_id = location_permissions.user_id " +
                "LEFT JOIN locations ON location_permissions.location_id = locations.location_id " +
                "WHERE location_permissions.location_id = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setInt(1, locationId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                UserDTO user = new UserDTOBuilder()
                        .withId(resultSet.getInt("id"))
                        .withFirstName(resultSet.getString("first_name"))
                        .withLastName(resultSet.getString("last_name"))
                        .withEmail(resultSet.getString("email"))
                        .withDateCreated(resultSet.getDate("dateCreated"))
                        .withVerified(resultSet.getBoolean("verified"))
                        .withLocationId(resultSet.getInt("locationId"))
                        .withLocation(resultSet.getString("location"))
                        .withCompViewAudit(resultSet.getInt("compViewAudit"))
                        .withCompAddRemoveUser(resultSet.getInt("compAddRemoveUser"))
                        .withCompManageUserCompanyPerm(resultSet.getInt("compManageUserCompanyPerm"))
                        .withCompChangeName(resultSet.getInt("compChangeName"))
                        .withCompAddRemoveProduct(resultSet.getInt("compAddRemoveProduct"))
                        .withCompEditProduct(resultSet.getInt("compEditProduct"))
                        .withLocViewAudit(resultSet.getInt("locViewAudit"))
                        .withLocAddUser(resultSet.getInt("locAddUser"))
                        .withLocRemoveUser(resultSet.getInt("locRemoveUser"))
                        .withLocManageUserLocationPerm(resultSet.getInt("locManageUserLocationPerm"))
                        .withLocChangeName(resultSet.getInt("locChangeName"))
                        .withLocChangeAddress(resultSet.getInt("locChangeAddress"))
                        .withLocViewStock(resultSet.getInt("locViewStock"))
                        .withLocManageStock(resultSet.getInt("locManageStock"))
                        .build();

                users.add(user);
            }
        }
        return users;

    }

    public User findUserByEmail(String email) {
        String SQL_QUERY = "SELECT * FROM users WHERE email = ? ;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return buildUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserByToken(String token) {
        String SQL_QUERY = "SELECT * FROM users WHERE verification_token = ? ;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return buildUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Select all users
    public List<User> selectAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(buildUserFromResultSet(resultSet));
            }

        }
        return users;
    }

    // Delete user by ID
    public boolean deleteUser(int id) throws SQLException {
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        }
    }

    // Get password from ID
    public String getPasswordFromId(String userId) {
        String SQL_QUERY = "SELECT password FROM users WHERE user_id = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("password");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Reset password using email
    public boolean resetPassword(String password, String email) {
        String SQL_QUERY = "UPDATE users SET password = ? WHERE email = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, password);
            preparedStatement.setString(2, email);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Check if email is taken
    public boolean isEmailTaken(String email) {
        String SQL_QUERY = "SELECT COUNT(*) FROM users WHERE email = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Validate login credentials using email and password
    public User validateLogin(String email, String password) {
        String SQL_QUERY = "SELECT * FROM users WHERE email = ? AND password = ?;";
        try (Connection connection = DBConnection.getConnectionToDatabase();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_QUERY)) {

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return buildUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Helper method to build User from ResultSet using the builder
    private User buildUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setID(resultSet.getInt("user_id"))
                .setFirstName(resultSet.getString("first_name"))
                .setLastName(resultSet.getString("last_name"))
                .setEmail(resultSet.getString("email"))
                .setPassword(resultSet.getString("password"))
                .setDateCreated(resultSet.getDate("date_created"))
                .setDateModified(resultSet.getDate("date_modified"))
                .setVerificationToken(resultSet.getString("verification_token"))
                .setVerified(resultSet.getBoolean("verified"))
                .setSalt(resultSet.getString("salt"))
                .build();
    }

}