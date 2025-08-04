package com.inventra.servlet;

import com.inventra.database.dao.CompanyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/company/delete/*" })
public class CompanyDeleteServlet extends HttpServlet {
    private final CompanyDAO companyDAO = new CompanyDAO();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // e.g "/company/delete/1"
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No company ID provided!");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(pathInfo.substring(1)); // get the location id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID!");
            return;
        }

        try {
            boolean deleted = companyDAO.deleteCompany(companyId);

            if (deleted) {
                response.getWriter().write("Company deleted successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Company not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete company");
        }

    }
}
