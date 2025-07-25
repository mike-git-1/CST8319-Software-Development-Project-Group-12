package database.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.beans.User;
import database.beans.UserBuilder;

public class UserDAO {

    private static final String INSERT_SQL =
        "INSERT INTO users " +
        "(first_name, last_name, email, password, date_created, verification_token, verified) VALUES " +
        "(?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_SQL = 
        "UPDATE users SET " +
        "first_name = ?, last_name = ?, email = ?, password = ?, date_modified = ? " +
        "WHERE user_id = ?;";

    private static final String SELECT_ID_SQL =
        "SELECT * FROM users WHERE user_id = ?;";

    private static final String SELECT_ALL_SQL =
        "SELECT * FROM users;";

    private static final String DELETE_SQL = 
        "DELETE FROM users WHERE user_id = ?;";

    public UserDAO() throws SQLException {}

    // Insert a new user with verification details
    public boolean insertUser(User user) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {

            preparedStatement.setString(1, user.getFirst_name());
            preparedStatement.setString(2, user.getLast_name());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setDate(5, user.getDate_created());
            preparedStatement.setString(6, user.getVerification_token());
            preparedStatement.setBoolean(7, user.isVerified());

            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
            preparedStatement.setInt(6, user.getId());

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
                return buildUserFromResultSet(resultSet);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Select all users
    public List<User> selectAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DBConnection.getConnectionToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL)) {

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                users.add(buildUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Delete user by ID
    public boolean deleteUser(int id) {
        try (Connection connection = DBConnection.getConnectionToDatabase();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_SQL)) {

            preparedStatement.setInt(1, id);
            return preparedStatement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
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
                .build();
    }
}