package com.inventra.model.beans;

import java.util.Date;

public class AuditDTO {

    private int auditId;
    private String location;
    private String user;
    private String changeType;
    private String changeTarget;
    private int changeTargetId;
    private String changeColumn;
    private String previousValue;
    private String newValue;
    private Date datetime;

    public AuditDTO(int auditId, String location, String user, int changeTargetId,
            String changeType, String changeTarget, String changeColumn, String previousValue, String newValue,
            Date datetime) {
        this.auditId = auditId;
        this.location = location;
        this.user = user;
        this.changeType = changeType;
        this.changeTarget = changeTarget;
        this.changeTargetId = changeTargetId;
        this.changeColumn = changeColumn;
        this.previousValue = previousValue;
        this.newValue = newValue;
        this.datetime = datetime;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocationId(String location) {
        this.location = location;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public String getChangeTarget() {
        return changeTarget;
    }

    public void setChangeTarget(String changeTarget) {
        this.changeTarget = changeTarget;
    }

    public int getChangeTargetId() {
        return changeTargetId;
    }

    public void setChangeTargetId(int changeTargetId) {
        this.changeTargetId = changeTargetId;
    }

    public String getChangeColumn() {
        return changeColumn;
    }

    public void setChangeColumn(String changeColumn) {
        this.changeColumn = changeColumn;
    }

    public String getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(String previousValue) {
        this.previousValue = previousValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

}