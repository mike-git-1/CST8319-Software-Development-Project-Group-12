package com.inventra.servlet;

import com.inventra.database.dao.LocationDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Company;
import com.inventra.model.beans.Location;
import com.inventra.model.builders.LocationBuilder;
import java.io.IOException;

@WebServlet(urlPatterns = { "/locations/*" })
public class LocationServlet extends HttpServlet {
    private final LocationDAO locationDAO = new LocationDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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
}
