package com.inventra.servlet;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.LocationDAO;
import com.inventra.database.dao.LocationPermissionsDAO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Location;
import com.inventra.model.builders.LocationBuilder;
import com.inventra.service.AuditService;
import com.inventra.service.SessionUtils;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/locations/*" })
public class LocationServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();
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

        try {
            request.setAttribute("locations", locationDAO.getLocationsByCompanyId(companyId));
            request.getRequestDispatcher("/locations-list.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch locations");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addLocation(request, response);
        } else if ("update".equals(action)) {
            updateLocation(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }
    }

    private void addLocation(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");

            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            if (locationId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Could not find user's location.\"}");
                return;
            }

            String name = request.getParameter("name");
            String address1 = request.getParameter("address_1");
            String address2 = request.getParameter("address_2");

            Location location = new LocationBuilder()
                    .withCompanyId(companyId)
                    .withName(name)
                    .withAddress1(address1)
                    .withAddress2(address2)
                    .build();

            int newLocationId = locationDAO.insertLocationAndReturnId(location);

            if (newLocationId == -1) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to create location.\"}");
                return;
            }

            auditService.logNewLocation(userId, companyId, newLocationId, location);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Location added successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error during location creation.\"}");
        }
    }

    private void updateLocation(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");

            int userLocationId = locationPermissionDao.getLocationIdByUserId(userId);
            if (userLocationId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Could not find user's location.\"}");
                return;
            }
            int locationId = Integer.parseInt(pathInfo.substring(1)); // get the location id from the url

            String name = request.getParameter("name");
            String address1 = request.getParameter("address_1");
            String address2 = request.getParameter("address_2");

            Location existingLocation = locationDAO.getLocationById(locationId);

            if (existingLocation == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Location not found!\"}");
                return;
            }

            Location updatedLocation = new LocationBuilder()
                    .withLocationId(locationId)
                    .withCompanyId(existingLocation.getCompanyId())
                    .withName(name)
                    .withAddress1(address1)
                    .withAddress2(address2)
                    .build();

            // existingLocation.setName(name);
            // existingLocation.setAddress1(address1);
            // existingLocation.setAddress2(address2);

            boolean updated = locationDAO.updateLocation(updatedLocation);

            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to update location.\"}");

                return;
            }

            auditService.logUpdatedLocation(userId, companyId, userLocationId, locationId, existingLocation,
                    updatedLocation);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Location updated successfully!\"}");
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
            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");

            int userLocationId = locationPermissionDao.getLocationIdByUserId(userId);
            if (userLocationId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Could not find user's location.\"}");
                return;
            }
            int locationId = Integer.parseInt(pathInfo.substring(1)); // get the location id from the url

            Location locationToDelete = locationDAO.getLocationById(locationId);

            if (locationToDelete == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Location not found!\"}");
                return;
            }

            boolean deleted = locationDAO.deleteLocation(locationId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to delete location.\"}");
                return;
            }

            auditService.logDeletedLocation(userId, companyId, userLocationId, locationId, locationToDelete);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Location deleted successfully.\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while deleting location.\"}");

        }

    }
}
