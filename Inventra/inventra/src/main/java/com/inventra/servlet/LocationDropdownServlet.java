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
import com.inventra.database.dao.LocationDAO;
import com.inventra.model.beans.Location;
import com.inventra.service.SessionUtils;

@WebServlet(urlPatterns = { "/location-dropdown" })
public class LocationDropdownServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

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
            List<Location> locations = locationDAO.getLocationsByCompanyId(companyId);

            Gson gson = new Gson();
            String json = gson.toJson(locations);
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(json);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}