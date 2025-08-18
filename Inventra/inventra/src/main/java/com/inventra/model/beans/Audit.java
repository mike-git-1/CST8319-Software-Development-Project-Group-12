package com.inventra.model.beans;

import com.inventra.model.enums.ChangeTypeEnum;

import java.util.Date;

import com.inventra.model.enums.ChangeTargetEnum;
// import java.sql.Date;

public class Audit {

    private int auditId;
    private int companyId;
    private int locationId;
    private int userId;
    private ChangeTypeEnum changeType;
    private ChangeTargetEnum changeTarget;
    private int changeTargetId;
    private String changeColumn;
    private String previousValue;
    private String newValue;
    private Date datetime;

    public Audit(int auditId, int companyId, int locationId, int userId, int changeTargetId, ChangeTypeEnum changeType,
            ChangeTargetEnum changeTarget, String changeColumn, String previousValue, String newValue, Date datetime) {
        this.auditId = auditId;
        this.companyId = companyId;
        this.locationId = locationId;
        this.userId = userId;
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public ChangeTypeEnum getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeTypeEnum changeType) {
        this.changeType = changeType;
    }

    public ChangeTargetEnum getChangeTarget() {
        return changeTarget;
    }

    public void setChangeTarget(ChangeTargetEnum changeTarget) {
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