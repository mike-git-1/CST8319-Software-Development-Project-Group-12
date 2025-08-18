package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.Employee;
import com.inventra.model.builders.EmployeeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class EmployeeDAO {

    public boolean insertEmployee(Employee employee) throws SQLException {
        String sql = "INSERT INTO employees (user_id, company_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employee.getUserId());
            stmt.setInt(2, employee.getCompanyId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteEmployee(Employee employee) throws SQLException {
        String sql = "DELETE FROM employees WHERE user_id = ? AND company_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, employee.getUserId());
            stmt.setInt(2, employee.getCompanyId());
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Employee> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE user_id = ?";
        List<Employee> worksFor = new ArrayList<>();

        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new EmployeeBuilder()
                            .withUserId(rs.getInt("user_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .build();
                    worksFor.add(employee);
                }
            }
        }
        return worksFor;
    }

    public List<Employee> getByCompanyId(int companyId) throws SQLException {
        String sql = "SELECT * FROM employees WHERE company_id = ?";
        List<Employee> employees = new ArrayList<>();

        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = new EmployeeBuilder()
                            .withUserId(rs.getInt("user_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .build();
                    employees.add(employee);
                }
            }
        }
        return employees;
    }

}