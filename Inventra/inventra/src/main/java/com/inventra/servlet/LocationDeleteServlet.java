package com.inventra.servlet;

import com.inventra.database.dao.LocationDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/location/delete/*" })
public class LocationDeleteServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
