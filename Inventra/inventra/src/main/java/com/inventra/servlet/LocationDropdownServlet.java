package com.inventra.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.LocationDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.Location;
import com.inventra.model.beans.LocationPermission;
import com.inventra.service.SessionUtils;

@WebServlet(urlPatterns = { "/location-dropdown" })
public class LocationDropdownServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();
    private final LocationPermissionsDAO locationPermissionDao = new LocationPermissionsDAO();
    private final CompanyPermissionsDAO companyPermissionDao = new CompanyPermissionsDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int userId = (Integer) session.getAttribute("userId");

            Gson gson = new Gson();

            CompanyPermission companyPermission = companyPermissionDao.getCompanyPermissionByUserId(userId);
            if (companyPermission != null && companyPermission.getManageUserCompanyPerm() > 0) {

                List<Location> locations = locationDAO.getLocationsByCompanyId(companyId);

                String json = gson.toJson(locations);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                return;
            }

            LocationPermission locationPermission = locationPermissionDao.getLocationPermissionsByUserId(userId);

            if (locationPermission != null && locationPermission.getManageUserLocationPerm() > 0) {
                Location location = locationDAO.getLocationById(locationPermission.getLocationId());

                String json = gson.toJson(location);
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write(json);
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}