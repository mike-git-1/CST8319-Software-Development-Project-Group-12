package com.inventra.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.database.dao.UserDAO;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.LocationPermission;
import com.inventra.model.beans.User;
import com.inventra.model.builders.CompanyPermissionBuilder;
import com.inventra.model.builders.LocationPermissionBuilder;
import com.inventra.model.builders.UserBuilder;
import com.inventra.service.AuditService;
import com.inventra.service.SessionUtils;

@WebServlet(urlPatterns = { "/users/*" })
public class UserServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();
    private final CompanyPermissionsDAO companyPermissionDao = new CompanyPermissionsDAO();
    private final LocationPermissionsDAO locationPermissionDao = new LocationPermissionsDAO();
    private final AuditService auditService = new AuditService(new AuditDAO());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        int companyId = (Integer) session.getAttribute("companyId");
        int userId = (Integer) session.getAttribute("userId");

        try {
            CompanyPermission companyPermission = companyPermissionDao.getCompanyPermissionByUserId(userId);

            // if user has company-wide permission manager access
            if (companyPermission.getAddRemoveUser() > 0
                    || companyPermission.getManageUserCompanyPerm() > 0) {
                request.setAttribute("users", userDao.getUsersByCompanyId(companyId));
                request.getRequestDispatcher("/users-list.jsp").forward(request, response);
                return;
            }

            LocationPermission locationPermission = locationPermissionDao
                    .getLocationPermissionsByUserId(userId);

            // if user has location-specific permission manager access
            if (locationPermission.getAddUser() > 0 || locationPermission.getRemoveUser() > 0
                    || locationPermission.getManageUserLocationPerm() > 0) {
                request.setAttribute("users",
                        userDao.getUsersByLocationId(locationPermission.getLocationId()));
                request.getRequestDispatcher("/users-list.jsp").forward(request, response);
                return;
            }

            // if user has no permission manager access
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "You do not have permission to view this page.");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch users");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");

        if ("add".equals(action)) {
            addUser(req, resp);
        } else if ("update".equals(action)) {
            updateUser(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }
    }

    private void addUser(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession(false);

        if (!SessionUtils.isUserLoggedIn(req)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int adminId = (Integer) session.getAttribute("userId");

            CompanyPermission adminCompanyPermission = companyPermissionDao.getCompanyPermissionByUserId(adminId);
            LocationPermission adminLocationPermission = locationPermissionDao.getLocationPermissionsByUserId(adminId);

            // Permission check FIRST
            if (adminCompanyPermission.getAddRemoveUser() <= 0 && adminLocationPermission.getAddUser() <= 0) {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                resp.getWriter()
                        .write("{\"success\": false, \"message\": \"You do not have permission to add users.\"}");
                return;
            }

            String email = req.getParameter("email");
            int locationId = Integer.parseInt(req.getParameter("locationId"));
            String token = UUID.randomUUID().toString();

            if (userDao.isEmailTaken(email)) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write(
                        "{\"success\": false, \"message\": \"This email is is already taken.\"}");
                return;
            }

            User user = new UserBuilder()
                    .setFirstName("--")
                    .setLastName("--")
                    .setEmail(email)
                    .setPassword("TEMP")
                    .setSalt("TEMP-SALT")
                    .setVerificationToken(token)
                    .setVerified(false)
                    .build();

            int newUserId = userDao.insertUserAndReturnId(user);

            if (newUserId < 0) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(
                        "{\"success\": false, \"message\": \"Failed to send invitation. User could not be created.\"}");
                return;
            }

            // User newUser = userDao.findUserByEmail(email);

            // if (newUser == null) {
            // resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            // resp.getWriter().write(
            // "{\"success\": false, \"message\": \"An error occurred. Failed to send
            // invitation.\"}");
            // return;
            // }

            CompanyPermission companyPermission = new CompanyPermissionBuilder()
                    .withCompanyId(companyId)
                    .withUserId(newUserId)
                    .build();

            boolean companyPermission_success = companyPermissionDao
                    .insertCompanyPermission(companyPermission);

            LocationPermission locationPermission = new LocationPermissionBuilder()
                    .withCompanyId(companyId)
                    .withLocationId(locationId)
                    .withUserId(newUserId)
                    .build();

            boolean locationPermission_success = locationPermissionDao
                    .insertLocationPermission(locationPermission);

            if (!companyPermission_success || !locationPermission_success) {
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                resp.getWriter().write(
                        "{\"success\": false, \"message\": \"Failed to send invitation. User profile could not be completed.\"}");
                return;
            }

            User newUser = userDao.selectUser(newUserId);

            if (newUser == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(
                        "{\"success\": false, \"message\": \"An error occurred. Could not find user.\"}");
                return;
            }

            auditService.logNewUser(adminId, companyId, adminLocationPermission.getLocationId(), newUserId,
                    newUser);

            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write(String.format(
                    "{\"success\": true, \"link\": \"http://localhost:8080/inventra/auth?token=%s\", \"message\": \"An invite has been sent to %s!\"}",
                    token, email));

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write("{\"success\": false, \"message\": \"Server error occurred.\"}");
        }

    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int adminId = (Integer) session.getAttribute("userId");

            CompanyPermission adminCompanyPermission = companyPermissionDao.getCompanyPermissionByUserId(adminId);
            LocationPermission adminLocationPermission = locationPermissionDao.getLocationPermissionsByUserId(adminId);

            // Permission check FIRST
            if (adminCompanyPermission.getManageUserCompanyPerm() <= 0
                    && adminLocationPermission.getManageUserLocationPerm() <= 0) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"You do not have permission to update users.\"}");
                return;
            }

            int userId = Integer.parseInt(pathInfo.substring(1));

            boolean updated1 = false;
            boolean updated2 = false;
            boolean updated3 = false;

            String lastName = request.getParameter("lastName");
            String firstName = request.getParameter("firstName");
            String email = request.getParameter("email");

            String hierarchy = request.getParameter("hierarchy");

            String locViewAudit = request.getParameter("locViewAudit");
            String locAddUser = request.getParameter("locAddUser");
            String locRemoveUser = request.getParameter("locRemoveUser");
            String locManageUserLocationPerm = request.getParameter("locManageUserLocationPerm");
            String locChangeName = request.getParameter("locChangeName");
            String locChangeAddress = request.getParameter("locChangeAddress");
            String locViewStock = request.getParameter("locViewStock");
            String locManageStock = request.getParameter("locManageStock");

            String compViewAudit = request.getParameter("compViewAudit");
            String compAddRemoveUser = request.getParameter("compAddRemoveUser");
            String compManageUserCompanyPerm = request.getParameter("compManageUserCompanyPerm");
            String compChangeName = request.getParameter("compChangeName");
            String compAddRemoveProduct = request.getParameter("compAddRemoveProduct");
            String compEditProduct = request.getParameter("compEditProduct");

            User existingUser = userDao.selectUserById(userId);

            if (existingUser != null) {
                existingUser.setLast_name(lastName);
                existingUser.setFirst_name(firstName);
                existingUser.setEmail(email);
                updated1 = userDao.updateUser(existingUser);
            }

            int locationId = Integer.parseInt(request.getParameter("locationId"));

            LocationPermission existingLocationPermission = locationPermissionDao
                    .getLocationPermissionsByUserId(userId);
            CompanyPermission existingCompanyPermission = companyPermissionDao
                    .getCompanyPermissionByUserId(userId);

            if (existingLocationPermission == null && existingCompanyPermission == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Could not locate user's profile.\"}");
                return;

            }

            LocationPermission updatedLocationPermission = new LocationPermissionBuilder()
                    .withUserId(userId)
                    .withLocationId(locationId)
                    .withCompanyId(existingLocationPermission.getCompanyId())
                    .withHierarchy(Integer.parseInt(hierarchy))
                    .withViewAudit(Integer.parseInt(locViewAudit))
                    .withAddUser(Integer.parseInt(locAddUser))
                    .withRemoveUser(Integer.parseInt(locRemoveUser))
                    .withManageUserLocationPerm(Integer.parseInt(locManageUserLocationPerm))
                    .withChangeName(Integer.parseInt(locChangeName))
                    .withChangeAddress(Integer.parseInt(locChangeAddress))
                    .withViewStock(Integer.parseInt(locViewStock))
                    .withManageStock(Integer.parseInt(locManageStock))
                    .build();

            updated2 = locationPermissionDao.updateLocationPermission(updatedLocationPermission);

            CompanyPermission updatedCompanyPermission = new CompanyPermissionBuilder()
                    .withUserId(userId)
                    .withCompanyId(existingCompanyPermission.getCompanyId())
                    .withViewAudit(Integer.parseInt(compViewAudit))
                    .withHierarchy(Integer.parseInt(hierarchy))
                    .withAddRemoveUser(Integer.parseInt(compAddRemoveUser))
                    .withManageUserCompanyPerm(Integer.parseInt(compManageUserCompanyPerm))
                    .withChangeName(Integer.parseInt(compChangeName))
                    .withAddRemoveProduct(Integer.parseInt(compAddRemoveProduct))
                    .withEditProduct(Integer.parseInt(compEditProduct))
                    .build();

            updated3 = companyPermissionDao.updateCompanyPermission(updatedCompanyPermission);

            if (!(updated1 && updated2 && updated3)) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to update user.\"}");

                return;

            }

            auditService.logUpdatedLocationPermission(adminId, companyId, adminLocationPermission.getLocationId(),
                    userId, existingLocationPermission, updatedLocationPermission);

            auditService.logUpdatedCompanyPermission(adminId, companyId, userId, existingCompanyPermission,
                    updatedCompanyPermission);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"User updated!\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while updating user.\"}");

        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int adminId = (Integer) session.getAttribute("userId");

            CompanyPermission adminCompanyPermission = companyPermissionDao.getCompanyPermissionByUserId(adminId);
            LocationPermission adminLocationPermission = locationPermissionDao.getLocationPermissionsByUserId(adminId);

            // Permission check FIRST
            if (adminCompanyPermission.getAddRemoveUser() <= 0 && adminLocationPermission.getRemoveUser() <= 0) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"You do not have permission to delete users.\"}");
                return;
            }

            int userId = Integer.parseInt(pathInfo.substring(1)); // get the user id from the url

            User userToDelete = userDao.selectUser(userId);

            if (userToDelete == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"User not found.\"}");
                return;
            }
            boolean deleted = userDao.deleteUser(userId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to delete user.\"}");
                return;
            }

            auditService.logDeletedUser(adminId, companyId, adminLocationPermission.getLocationId(), userId,
                    userToDelete);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"User deleted!\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while deleting user.\"}");

        }
    }

}
