package com.inventra.service;

import java.sql.Date;

import com.google.gson.Gson;
import com.inventra.database.dao.AuditDAO;
import com.inventra.model.builders.AuditBuilder;
import com.inventra.model.enums.ChangeTargetEnum;
import com.inventra.model.enums.ChangeTypeEnum;
import com.inventra.model.beans.Audit;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.Inventory;
import com.inventra.model.beans.Location;
import com.inventra.model.beans.LocationPermission;
import com.inventra.model.beans.Product;
import com.inventra.model.beans.User;

public class AuditService {
    private final AuditDAO auditDao;

    public AuditService(AuditDAO auditDao) {
        this.auditDao = auditDao;
    }

    public void logChange(int userId, int companyId, int locationId, ChangeTypeEnum changeType,
            ChangeTargetEnum changeTarget,
            int changeTargetId, String changeColumn,
            String previousValue, String newValue, Date datetime) {

        Audit audit = new AuditBuilder()
                .withUserId(userId)
                .withCompanyId(companyId)
                .withLocationId(locationId)
                .withChangeType(changeType)
                .withChangeTarget(changeTarget)
                .withChangeTargetId(changeTargetId)
                .withChangeColumn(changeColumn)
                .withPreviousValue(previousValue)
                .withNewValue(newValue)
                .withDatetime(datetime)
                .build();
        try {
            if (locationId == -1) {
                auditDao.insertAuditNoLocation(audit);
                return;
            }
            auditDao.insertAuditWithLocation(audit);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Audit insert failed: " + e.getMessage());
        }
    }

    public void logNewProduct(int userId, int companyId, int changeTargetId,
            Product product) {

        Date now = new Date(System.currentTimeMillis()); // get current date

        logChange(userId, companyId, -1, ChangeTypeEnum.ADDED, ChangeTargetEnum.PRODUCT,
                changeTargetId, "Object", "-", new Gson().toJson(product), now);

        // logChange(userId, companyId, -1, ChangeTypeEnum.ADDED,
        // ChangeTargetEnum.PRODUCT,
        // changeTargetId, "description", "-", product.getDescription(), now);

        // logChange(userId, companyId, -1, ChangeTypeEnum.ADDED,
        // ChangeTargetEnum.PRODUCT,
        // changeTargetId, "price", "-", String.valueOf(product.getPrice()), now);
    }

    public void logUpdatedProduct(int userId, int companyId, int locationId, int changeTargetId,
            Product previousProduct, Product newProduct) {

        Date now = new Date(System.currentTimeMillis()); // get current date

        // check if any of the fields have changed
        if (!previousProduct.getName().equals(newProduct.getName())) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.PRODUCT,
                    changeTargetId, "Name", previousProduct.getName(), newProduct.getName(), now);
        }

        if (!previousProduct.getDescription().equals(newProduct.getDescription())) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.PRODUCT,
                    changeTargetId, "Description", previousProduct.getDescription(), newProduct.getDescription(), now);
        }

        if (previousProduct.getPrice() != newProduct.getPrice()) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.PRODUCT,
                    changeTargetId, "Price", String.valueOf(previousProduct.getPrice()),
                    String.valueOf(newProduct.getPrice()), now);
        }

    }

    public void logDeletedProduct(int userId, int companyId, int locationId, int changeTargetId,
            Product product) {
        Date now = new Date(System.currentTimeMillis());
        logChange(userId, companyId, locationId, ChangeTypeEnum.REMOVED, ChangeTargetEnum.PRODUCT,
                changeTargetId, "Object",
                new Gson().toJson(product), "-", now); // pass serialized product as previous value

    }

    public void logNewInventory(int userId, int companyId, int locationId, int changeTargetId,
            Inventory inventory) {

        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, locationId, ChangeTypeEnum.ADDED, ChangeTargetEnum.INVENTORY,
                changeTargetId, "Object", "-", new Gson().toJson(inventory), now);

    }

    public void logUpdatedInventory(int userId, int companyId, int locationId, int changeTargetId,
            int oldStock, int newStock) {

        if (oldStock != newStock) {
            Date now = new Date(System.currentTimeMillis()); // get current date
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.INVENTORY,
                    changeTargetId, "Stock", String.valueOf(oldStock), String.valueOf(newStock), now);
        }

    }

    public void logDeletedInventory(int userId, int companyId, int locationId, int changeTargetId,
            Inventory inventory) {

        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, locationId, ChangeTypeEnum.REMOVED, ChangeTargetEnum.INVENTORY,
                changeTargetId, "Object", new Gson().toJson(inventory), "-", now);

    }

    public void logNewLocation(int userId, int companyId, int changeTargetId,
            Location location) {

        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, -1, ChangeTypeEnum.ADDED, ChangeTargetEnum.LOCATION,
                changeTargetId, "Object", "-", new Gson().toJson(location), now);

    }

    public void logUpdatedLocation(int userId, int companyId, int locationId, int changeTargetId,
            Location oldLocation, Location newLocation) {

        Date now = new Date(System.currentTimeMillis()); // get current date

        if (!oldLocation.getName().equals(newLocation.getName())) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.LOCATION,
                    changeTargetId, "Name", oldLocation.getName(), newLocation.getName(), now);
        }

        if (!oldLocation.getAddress1().equals(newLocation.getAddress1())) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.LOCATION,
                    changeTargetId, "Address 1", oldLocation.getAddress1(), newLocation.getAddress1(), now);
        }

        if (!oldLocation.getAddress2().equals(newLocation.getAddress2())) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.LOCATION,
                    changeTargetId, "Address 2", oldLocation.getAddress2(), newLocation.getAddress2(), now);
        }

    }

    public void logDeletedLocation(int userId, int companyId, int locationId, int changeTargetId,
            Location location) {

        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, locationId, ChangeTypeEnum.REMOVED, ChangeTargetEnum.LOCATION,
                changeTargetId, "Object", new Gson().toJson(location), "-", now);
    }

    public void logNewUser(int userId, int companyId, int locationId, int changeTargetId, User user) {
        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, locationId, ChangeTypeEnum.ADDED, ChangeTargetEnum.USER,
                changeTargetId, "Object", "-", new Gson().toJson(user), now);
        System.out.println("New User: " + new Gson().toJson(user));
    }

    public void logDeletedUser(int userId, int companyId, int locationId, int changeTargetId,
            User user) {

        Date now = new Date(System.currentTimeMillis()); // get current date
        logChange(userId, companyId, locationId, ChangeTypeEnum.REMOVED, ChangeTargetEnum.USER,
                changeTargetId, "Object", new Gson().toJson(user), "-", now);
    }

    public void logUpdatedLocationPermission(int userId, int companyId, int locationId, int changeTargetId,
            LocationPermission oldPerm, LocationPermission newPerm) {
        Date now = new Date(System.currentTimeMillis()); // get current date

        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Location ID", oldPerm.getLocationId(),
                newPerm.getLocationId(), now);

        checkPermissionChange(userId, companyId, locationId, changeTargetId, "View Audit (L)", oldPerm.getViewAudit(),
                newPerm.getViewAudit(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Add User (L)", oldPerm.getAddUser(),
                newPerm.getAddUser(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Remove User (L)", oldPerm.getRemoveUser(),
                newPerm.getRemoveUser(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Permission Manager (L)",
                oldPerm.getManageUserLocationPerm(), newPerm.getManageUserLocationPerm(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Change Name (L)", oldPerm.getChangeName(),
                newPerm.getChangeName(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Change Address (L)",
                oldPerm.getChangeAddress(), newPerm.getChangeAddress(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "View Stock (L)", oldPerm.getViewStock(),
                newPerm.getViewStock(), now);
        checkPermissionChange(userId, companyId, locationId, changeTargetId, "Manage Stock (L)",
                oldPerm.getManageStock(),
                newPerm.getManageStock(), now);

    }

    public void logUpdatedCompanyPermission(int userId, int companyId, int changeTargetId,
            CompanyPermission oldPerm, CompanyPermission newPerm) {
        Date now = new Date(System.currentTimeMillis()); // get current date
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Hierarchy", oldPerm.getHierarchy(),
                newPerm.getHierarchy(), now);

        checkPermissionChange(userId, companyId, -1, changeTargetId, "View Audit (C)", oldPerm.getViewAudit(),
                newPerm.getViewAudit(), now);
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Add/Remove Users (C)", oldPerm.getAddRemoveUser(),
                newPerm.getAddRemoveUser(), now);
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Permission Manager (C)",
                oldPerm.getManageUserCompanyPerm(), newPerm.getManageUserCompanyPerm(), now);
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Change Name (C)", oldPerm.getChangeName(),
                newPerm.getChangeName(), now);
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Add/Remove Products (C)",
                oldPerm.getAddRemoveProduct(),
                newPerm.getAddRemoveProduct(), now);
        checkPermissionChange(userId, companyId, -1, changeTargetId, "Edit Products (C)", oldPerm.getEditProduct(),
                newPerm.getEditProduct(), now);

    }

    public void logUpdatedCompany(int userId, int companyId, int changeTargetId, String oldValue, String newValue) {
        Date now = new Date(System.currentTimeMillis()); // get current date
        if (!oldValue.equals(newValue)) {
            logChange(userId, companyId, -1, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.COMPANY,
                    changeTargetId, "Name", oldValue, newValue, now);
        }
    }

    private void checkPermissionChange(
            int userId, int companyId, int locationId, int changeTargetId,
            String propertyName, int oldVal, int newVal, Date now) {
        if (oldVal != newVal) {
            logChange(userId, companyId, locationId, ChangeTypeEnum.MODIFIED, ChangeTargetEnum.PERMISSION,
                    changeTargetId, propertyName, String.valueOf(oldVal), String.valueOf(newVal), now);
        }
    }
}
