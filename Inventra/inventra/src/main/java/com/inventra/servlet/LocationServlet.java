package com.inventra.servlet;

import com.inventra.database.dao.LocationDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Company;
import com.inventra.model.beans.Location;
import com.inventra.model.builders.LocationBuilder;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/locations/*" })
public class LocationServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Usernot logged in!");
            return;
        }

        // session returns object. Cast to Company
        Company company = (Company) session.getAttribute("company");
        int companyId = company.getCompanyId();

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
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Usernot logged in!");
            return;
        }

        // session returns object. Cast to Company
        Company company = (Company) session.getAttribute("company");
        int companyId = company.getCompanyId();

        try {
            String name = request.getParameter("name");
            String address1 = request.getParameter("address_1");
            String address2 = request.getParameter("address_2");

            Location location = new LocationBuilder()
                    .withCompanyId(companyId)
                    .withName(name)
                    .withAddress1(address1)
                    .withAddress2(address2)
                    .build();

            boolean success = locationDAO.insertLocation(location);
            response.getWriter().write(success ? "Location created!" : "Failed to create location!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server error during location creation!");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // id part of the url, e.g "/location/update/123"

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No location ID provided!");
            return;
        }

        int locationId;
        try {
            locationId = Integer.parseInt(pathInfo.substring(1)); // get the location id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID!");
            return;
        }

        try {
            String name = request.getParameter("name");
            String address1 = request.getParameter("address_1");
            String address2 = request.getParameter("address_2");

            Location existingLocation = locationDAO.getLocationById(locationId);

            if (existingLocation == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found!");
                return;
            }

            existingLocation.setName(name);
            existingLocation.setAddress1(address1);
            existingLocation.setAddress2(address2);

            boolean updated = locationDAO.updateLocation(existingLocation);

            if (updated) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Location updated successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update locatoin!");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format!");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating location!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // e.g "/location/delete/1"
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No location ID provided!");
            return;
        }

        int locationId;
        try {
            locationId = Integer.parseInt(pathInfo.substring(1)); // get the location id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid location ID!");
            return;
        }

        try {
            boolean deleted = locationDAO.deleteLocation(locationId);

            if (deleted) {
                response.getWriter().write("Location deleted successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Location not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete location");
        }

    }
}
