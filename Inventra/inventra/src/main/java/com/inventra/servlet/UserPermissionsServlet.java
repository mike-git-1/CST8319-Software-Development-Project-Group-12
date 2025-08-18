package com.inventra.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.google.gson.JsonObject;
import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.LocationPermission;
import com.inventra.service.SessionUtils;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// for frontend to get user permissions
@WebServlet("/user/permissions")
public class UserPermissionsServlet extends HttpServlet {
    private CompanyPermissionsDAO companyPermsDao = new CompanyPermissionsDAO();
    private LocationPermissionsDAO locationPermsDao = new LocationPermissionsDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try {
            CompanyPermission companyPerms = companyPermsDao.getCompanyPermissionByUserId(userId);
            LocationPermission locationPerms = locationPermsDao.getLocationPermissionsByUserId(userId);

            if (companyPerms == null || locationPerms == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            JsonObject result = new JsonObject();
            result.addProperty("companyHierarchy", companyPerms.getHierarchy());
            result.addProperty("canAddRemoveUser", companyPerms.getAddRemoveUser());
            result.addProperty("canViewCompanyAudit", companyPerms.getViewAudit());
            result.addProperty("canManageUserCompanyPerm", companyPerms.getManageUserCompanyPerm());
            result.addProperty("canChangeCompanyName", companyPerms.getChangeName());
            result.addProperty("canAddRemoveProduct", companyPerms.getAddRemoveProduct());
            result.addProperty("canEditProduct", companyPerms.getEditProduct());

            result.addProperty("locationHierarchy", locationPerms.getHierarchy());
            result.addProperty("canViewLocationAudit", locationPerms.getViewAudit());
            result.addProperty("canAddUser", locationPerms.getAddUser());
            result.addProperty("canRemoveUser", locationPerms.getRemoveUser());
            result.addProperty("canManageUserLocationPerm", locationPerms.getManageUserLocationPerm());
            result.addProperty("canChangeLocationName", locationPerms.getChangeName());
            result.addProperty("canChangeLocationAddress", locationPerms.getChangeAddress());
            result.addProperty("canViewStock", locationPerms.getViewStock());
            result.addProperty("canManageStock", locationPerms.getManageStock());

            response.setContentType("application/json");
            response.getWriter().write(result.toString());

        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            e.printStackTrace();
        }

    }
}