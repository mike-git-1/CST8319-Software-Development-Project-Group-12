package com.inventra.database.dao;

import com.inventra.database.DBConnection;
import com.inventra.model.beans.Audit;
import com.inventra.model.builders.AuditBuilder;
import com.inventra.model.enums.ChangeTargetEnum;
import com.inventra.model.enums.ChangeTypeEnum;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class AuditDAO {
    
    public boolean insertAuditNoLocation(Audit audit) throws SQLException {
        String sql = "INSERT INTO audit (company_id, user_id, change_type, change_target, change_target_id, " +
        "change_column, previous_value, new_value, datetime) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, audit.getCompanyId());
            stmt.setInt(2, audit.getUserId());
            stmt.setString(3, audit.getChangeType().name());
            stmt.setString(4, audit.getChangeTarget().name());
            stmt.setInt(5, audit.getChangeTargetId());
            stmt.setString(6, audit.getChangeColumn());
            stmt.setString(7, audit.getPreviousValue());
            stmt.setString(8, audit.getNewValue());
            stmt.setDate(9, audit.getDatetime());
            return stmt.executeUpdate() > 0;
        }
    }

        public boolean insertAuditWithLocation(Audit audit) throws SQLException {
        String sql = "INSERT INTO audit (company_id, user_id, change_type, change_target, change_target_id, " +
        "change_column, previous_value, new_value, datetime, location_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, audit.getCompanyId());
            stmt.setInt(2, audit.getUserId());
            stmt.setString(3, audit.getChangeType().name());
            stmt.setString(4, audit.getChangeTarget().name());
            stmt.setInt(5, audit.getChangeTargetId());
            stmt.setString(6, audit.getChangeColumn());
            stmt.setString(7, audit.getPreviousValue());
            stmt.setString(8, audit.getNewValue());
            stmt.setDate(9, audit.getDatetime());
            stmt.setInt(10, audit.getLocationId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean updateAudit(Audit audit) throws SQLException {
        String sql = "UPDATE audit SET company_id = ?, location_id = ?, user_id = ?, change_type = ?, change_target = ?, change_target_id = ?, " +
        "change_column = ?, previous_value = ?, new_value = ?, datetime = ? WHERE audit_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, audit.getCompanyId());
                    stmt.setInt(2, audit.getLocationId());
                    stmt.setInt(3, audit.getUserId());
                    stmt.setString(4, audit.getChangeType().name());
                    stmt.setString(5, audit.getChangeTarget().name());
                    stmt.setInt(6, audit.getChangeTargetId());
                    stmt.setString(7, audit.getChangeColumn());
                    stmt.setString(8, audit.getPreviousValue());
                    stmt.setString(9, audit.getNewValue());
                    stmt.setDate(10, audit.getDatetime());
                    return stmt.executeUpdate() > 0;
                }
    }

    public Audit getByAuditId(int id) throws SQLException {
        String sql = "SELECT * FROM audit WHERE audit_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String changeTypeStr = rs.getString("change_type");
                    ChangeTypeEnum changeType = (changeTypeStr == null)
                        ? null
                        : ChangeTypeEnum.valueOf(changeTypeStr.trim().toUpperCase());

                    String changeTargetStr = rs.getString("change_target");
                    ChangeTargetEnum changeTarget = (changeTargetStr == null)
                        ? null
                        : ChangeTargetEnum.valueOf(changeTargetStr.trim().toUpperCase());
                    
                    return new AuditBuilder()
                            .withAuditId(rs.getInt("audit_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withLocationId(rs.getInt("location_id"))
                            .withUserId(rs.getInt("user_id"))
                            .withChangeType(changeType)
                            .withChangeTarget(changeTarget)
                            .withChangeTargetId(rs.getInt("target_get_int"))
                            .withChangeColumn(rs.getString("change_column"))
                            .withPreviousValue(rs.getString("previous_value"))
                            .withNewValue(rs.getString("new_value"))
                            .withDatetime(rs.getDate("datetime"))
                            .build();
                }
            return null;
            }
        }
    }

    public List<Audit> getByCompanyId(int id) throws SQLException {
        String sql = "SELECT * FROM audit WHERE company_id = ?";
        List<Audit> audits = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String changeTypeStr = rs.getString("change_type");
                    ChangeTypeEnum changeType = (changeTypeStr == null)
                        ? null
                        : ChangeTypeEnum.valueOf(changeTypeStr.trim().toUpperCase());

                    String changeTargetStr = rs.getString("change_target");
                    ChangeTargetEnum changeTarget = (changeTargetStr == null)
                        ? null
                        : ChangeTargetEnum.valueOf(changeTargetStr.trim().toUpperCase());
                    
                    Audit audit = new AuditBuilder()
                            .withAuditId(rs.getInt("audit_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withLocationId(rs.getInt("location_id"))
                            .withUserId(rs.getInt("user_id"))
                            .withChangeType(changeType)
                            .withChangeTarget(changeTarget)
                            .withChangeTargetId(rs.getInt("target_get_int"))
                            .withChangeColumn(rs.getString("change_column"))
                            .withPreviousValue(rs.getString("previous_value"))
                            .withNewValue(rs.getString("new_value"))
                            .withDatetime(rs.getDate("datetime"))
                            .build();
                    audits.add(audit);
                }
            return null;
            }
        }
    }

    public List<Audit> getByLocationId(int id) throws SQLException {
        String sql = "SELECT * FROM audit WHERE location_id = ?";
        List<Audit> audits = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String changeTypeStr = rs.getString("change_type");
                    ChangeTypeEnum changeType = (changeTypeStr == null)
                        ? null
                        : ChangeTypeEnum.valueOf(changeTypeStr.trim().toUpperCase());

                    String changeTargetStr = rs.getString("change_target");
                    ChangeTargetEnum changeTarget = (changeTargetStr == null)
                        ? null
                        : ChangeTargetEnum.valueOf(changeTargetStr.trim().toUpperCase());
                    
                    Audit audit = new AuditBuilder()
                            .withAuditId(rs.getInt("audit_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withLocationId(rs.getInt("location_id"))
                            .withUserId(rs.getInt("user_id"))
                            .withChangeType(changeType)
                            .withChangeTarget(changeTarget)
                            .withChangeTargetId(rs.getInt("target_get_int"))
                            .withChangeColumn(rs.getString("change_column"))
                            .withPreviousValue(rs.getString("previous_value"))
                            .withNewValue(rs.getString("new_value"))
                            .withDatetime(rs.getDate("datetime"))
                            .build();
                    audits.add(audit);
                }
            return null;
            }
        }
    }

    public List<Audit> getByUserAndCompanyId(int userId, int companyId) throws SQLException {
        String sql = "SELECT * FROM audit WHERE user_id = ? AND company_id = ?";
        List<Audit> audits = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, companyId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String changeTypeStr = rs.getString("change_type");
                    ChangeTypeEnum changeType = (changeTypeStr == null)
                        ? null
                        : ChangeTypeEnum.valueOf(changeTypeStr.trim().toUpperCase());

                    String changeTargetStr = rs.getString("change_target");
                    ChangeTargetEnum changeTarget = (changeTargetStr == null)
                        ? null
                        : ChangeTargetEnum.valueOf(changeTargetStr.trim().toUpperCase());
                    
                    Audit audit = new AuditBuilder()
                            .withAuditId(rs.getInt("audit_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withLocationId(rs.getInt("location_id"))
                            .withUserId(rs.getInt("user_id"))
                            .withChangeType(changeType)
                            .withChangeTarget(changeTarget)
                            .withChangeTargetId(rs.getInt("target_get_int"))
                            .withChangeColumn(rs.getString("change_column"))
                            .withPreviousValue(rs.getString("previous_value"))
                            .withNewValue(rs.getString("new_value"))
                            .withDatetime(rs.getDate("datetime"))
                            .build();
                    audits.add(audit);
                }
            return null;
            }
        }
    }

    public List<Audit> getByUserAndLocationId(int userId, int locationId) throws SQLException {
        String sql = "SELECT * FROM audit WHERE user_id = ? AND location_id = ?";
        List<Audit> audits = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, locationId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String changeTypeStr = rs.getString("change_type");
                    ChangeTypeEnum changeType = (changeTypeStr == null)
                        ? null
                        : ChangeTypeEnum.valueOf(changeTypeStr.trim().toUpperCase());

                    String changeTargetStr = rs.getString("change_target");
                    ChangeTargetEnum changeTarget = (changeTargetStr == null)
                        ? null
                        : ChangeTargetEnum.valueOf(changeTargetStr.trim().toUpperCase());
                    
                    Audit audit = new AuditBuilder()
                            .withAuditId(rs.getInt("audit_id"))
                            .withCompanyId(rs.getInt("company_id"))
                            .withLocationId(rs.getInt("location_id"))
                            .withUserId(rs.getInt("user_id"))
                            .withChangeType(changeType)
                            .withChangeTarget(changeTarget)
                            .withChangeTargetId(rs.getInt("target_get_int"))
                            .withChangeColumn(rs.getString("change_column"))
                            .withPreviousValue(rs.getString("previous_value"))
                            .withNewValue(rs.getString("new_value"))
                            .withDatetime(rs.getDate("datetime"))
                            .build();
                    audits.add(audit);
                }
            return null;
            }
        }
    }

    public boolean deleteAudit(int auditId) throws SQLException {
        String sql = "DELETE FROM audit WHERE audit_id = ?";
        try (Connection conn = DBConnection.getConnectionToDatabase();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, auditId);
            return stmt.executeUpdate() > 0;
        }
    }

}
