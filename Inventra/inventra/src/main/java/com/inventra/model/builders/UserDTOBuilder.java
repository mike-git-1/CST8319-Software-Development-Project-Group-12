package com.inventra.model.builders;

import com.inventra.model.beans.UserDTO;
import java.sql.Date;

public class UserDTOBuilder {
    private final UserDTO user = new UserDTO();

    public UserDTOBuilder withId(int id) {
        user.setId(id);
        return this;
    }

    public UserDTOBuilder withFirstName(String firstName) {
        user.setFirst_name(firstName);
        return this;
    }

    public UserDTOBuilder withLastName(String lastName) {
        user.setLast_name(lastName);
        return this;
    }

    public UserDTOBuilder withEmail(String email) {
        user.setEmail(email);
        return this;
    }

    public UserDTOBuilder withLocation(String location) {
        user.setLocation(location);
        return this;
    }

    public UserDTOBuilder withLocationId(int locationId) {
        user.setLocationId(locationId);
        return this;
    }

    public UserDTOBuilder withDateCreated(Date dateCreated) {
        user.setDate_created(dateCreated);
        return this;
    }

    public UserDTOBuilder withVerified(boolean verified) {
        user.setVerified(verified);
        return this;
    }

    public UserDTOBuilder withCompViewAudit(int value) {
        user.setCompViewAudit(value);
        return this;
    }

    public UserDTOBuilder withCompAddRemoveUser(int value) {
        user.setCompAddRemoveUser(value);
        return this;
    }

    public UserDTOBuilder withCompManageUserCompanyPerm(int value) {
        user.setCompManageUserCompanyPerm(value);
        return this;
    }

    public UserDTOBuilder withCompChangeName(int value) {
        user.setCompChangeName(value);
        return this;
    }

    public UserDTOBuilder withCompAddRemoveProduct(int value) {
        user.setCompAddRemoveProduct(value);
        return this;
    }

    public UserDTOBuilder withCompEditProduct(int value) {
        user.setCompEditProduct(value);
        return this;
    }

    public UserDTOBuilder withLocViewAudit(int value) {
        user.setLocViewAudit(value);
        return this;
    }

    public UserDTOBuilder withLocAddUser(int value) {
        user.setLocAddUser(value);
        return this;
    }

    public UserDTOBuilder withLocRemoveUser(int value) {
        user.setLocRemoveUser(value);
        return this;
    }

    public UserDTOBuilder withLocManageUserLocationPerm(int value) {
        user.setLocManageUserLocationPerm(value);
        return this;
    }

    public UserDTOBuilder withLocChangeName(int value) {
        user.setLocChangeName(value);
        return this;
    }

    public UserDTOBuilder withLocChangeAddress(int value) {
        user.setLocChangeAddress(value);
        return this;
    }

    public UserDTOBuilder withLocViewStock(int value) {
        user.setLocViewStock(value);
        return this;
    }

    public UserDTOBuilder withLocManageStock(int value) {
        user.setLocManageStock(value);
        return this;
    }

    public UserDTO build() {
        return user;
    }
}
