package com.inventra.model.builders;

import java.util.Date;

import com.inventra.model.beans.AuditDTO;

public class AuditDTOBuilder {

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

    public AuditDTOBuilder withAuditId(int auditId) {
        this.auditId = auditId;
        return this;
    }

    public AuditDTOBuilder withLocation(String location) {
        this.location = location;
        return this;
    }

    public AuditDTOBuilder withUser(String user) {
        this.user = user;
        return this;
    }

    public AuditDTOBuilder withChangeType(String changeType) {
        this.changeType = changeType;
        return this;
    }

    public AuditDTOBuilder withChangeTarget(String changeTarget) {
        this.changeTarget = changeTarget;
        return this;
    }

    public AuditDTOBuilder withChangeTargetId(int changeTargetId) {
        this.changeTargetId = changeTargetId;
        return this;
    }

    public AuditDTOBuilder withChangeColumn(String changeColumn) {
        this.changeColumn = changeColumn;
        return this;
    }

    public AuditDTOBuilder withPreviousValue(String previousValue) {
        this.previousValue = previousValue;
        return this;
    }

    public AuditDTOBuilder withNewValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public AuditDTOBuilder withDatetime(Date datetime) {
        this.datetime = datetime;
        return this;
    }

    public AuditDTO build() {
        return new AuditDTO(auditId, location, user, changeTargetId, changeType, changeTarget,
                changeColumn, previousValue, newValue, datetime);
    }

}