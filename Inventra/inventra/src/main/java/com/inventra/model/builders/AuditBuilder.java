package com.inventra.model.builders;

import java.sql.Date;
import com.inventra.model.enums.ChangeTypeEnum;
import com.inventra.model.enums.ChangeTargetEnum;
import com.inventra.model.beans.Audit;

public class AuditBuilder {
    
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

    public AuditBuilder withAuditId(int auditId) {
        this.auditId = auditId;
        return this;
    }

    public AuditBuilder withCompanyId(int companyId) {
        this.companyId = companyId;
        return this;
    }

    public AuditBuilder withLocationId(int locationId) {
        this.locationId = locationId;
        return this;
    }
    
    public AuditBuilder withUserId(int userId) {
        this.userId = userId;
        return this;
    }

    public AuditBuilder withChangeType(ChangeTypeEnum changeType) {
        this.changeType = changeType;
        return this;
    }

    public AuditBuilder withChangeTarget(ChangeTargetEnum changeTarget) {
        this.changeTarget = changeTarget;
        return this;
    }

    public AuditBuilder withChangeTargetId(int changeTargetId) {
        this.changeTargetId = changeTargetId;
        return this;
    }

    public AuditBuilder withChangeColumn(String changeColumn) {
        this.changeColumn = changeColumn;
        return this;
    }

    public AuditBuilder withPreviousValue(String previousValue) {
        this.previousValue = previousValue;
        return this;
    }

    public AuditBuilder withNewValue(String newValue) {
        this.newValue = newValue;
        return this;
    }

    public AuditBuilder withDatetime(Date datetime) {
        this.datetime = datetime;
        return this;
    }

    public Audit build() {
        return new Audit(auditId, companyId, locationId, userId, changeTargetId, changeType, changeTarget,
    changeColumn, previousValue, newValue, datetime);
    }

}
