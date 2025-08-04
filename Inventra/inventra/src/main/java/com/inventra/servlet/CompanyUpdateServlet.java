package com.inventra.servlet;

import com.inventra.database.dao.CompanyDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Company;
import com.inventra.model.beans.User;

import java.io.IOException;

@WebServlet(urlPatterns = { "/company/update/*" })
public class CompanyUpdateServlet extends HttpServlet {
    private final CompanyDAO companyDAO = new CompanyDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        // session returns object. Cast to User
        User admin = (User) session.getAttribute("user");

        if (admin == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        String pathInfo = request.getPathInfo(); // id part of the url, e.g "/company/update/123"

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No company ID provided!");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(pathInfo.substring(1)); // get the company id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID!");
            return;
        }

        try {
            String name = request.getParameter("name");

            Company existingCompany = companyDAO.getCompanyById(companyId);

            if (existingCompany == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Company not found!");
                return;
            }

            existingCompany.setName(name);
            existingCompany.setAdminId(admin.getId());

            boolean updated = companyDAO.updateCompany(existingCompany);

            if (updated) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Company updated successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update company!");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format!");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating company!");
        }
    }
}
