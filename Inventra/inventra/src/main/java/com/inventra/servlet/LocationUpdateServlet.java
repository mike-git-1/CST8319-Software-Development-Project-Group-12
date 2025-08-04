package com.inventra.servlet;

import com.inventra.database.dao.LocationDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Location;
import java.io.IOException;

@WebServlet(urlPatterns = { "/location/update/*" })
public class LocationUpdateServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
}
