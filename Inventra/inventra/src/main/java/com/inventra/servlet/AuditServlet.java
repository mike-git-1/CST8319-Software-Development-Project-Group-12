package com.inventra.servlet;

import java.io.IOException;
import java.sql.SQLException;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.LocationPermission;
import com.inventra.service.SessionUtils;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet(urlPatterns = { "/audit/*" })
public class AuditServlet extends HttpServlet {
    private final AuditDAO auditDao = new AuditDAO();
    private final CompanyPermissionsDAO companyPermissionDao = new CompanyPermissionsDAO();
    private final LocationPermissionsDAO locationPermissionDao = new LocationPermissionsDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int userId = (Integer) session.getAttribute("userId");
            CompanyPermission companyPermission = companyPermissionDao.getCompanyPermissionByUserId(userId);

            // if user has company-wide audit access
            if (companyPermission.getViewAudit() > 0) {
                request.setAttribute("audit_logs", auditDao.getByCompanyId(companyId));
                request.getRequestDispatcher("/audit-list.jsp").forward(request, response);
                return;
            }

            LocationPermission locationPermission = locationPermissionDao
                    .getLocationPermissionsByUserId(userId);

            // if user has location-specific permission manager access
            if (locationPermission.getViewAudit() > 0) {
                request.setAttribute("audit_logs", auditDao.getByLocationId(locationPermission.getLocationId()));
                request.getRequestDispatcher("/audit-list.jsp").forward(request, response);
                return;
            }

            // if user has no permission manager access
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to view this page.");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch audit logs.");
        }

    }
}