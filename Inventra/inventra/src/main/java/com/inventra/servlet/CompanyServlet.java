package com.inventra.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import com.inventra.database.dao.CompanyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Company;
import com.inventra.model.beans.User;
import com.inventra.model.builders.CompanyBuilder;

@WebServlet(urlPatterns = { "/companies/*" })
public class CompanyServlet extends HttpServlet {
    private final CompanyDAO companyDAO = new CompanyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }
        try {
            request.setAttribute("companies", companyDAO.getAllCompanies());
            request.getRequestDispatcher("/companies-list.jsp").forward(request, response);
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
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        User admin = (User) session.getAttribute("user");

        if (admin == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        String name = request.getParameter("name");
        int adminId = admin.getId();

        Company company = new CompanyBuilder()
                .withName(name)
                .withAdminId(adminId)
                .withDateCreated(new Date())
                .build();

        try {
            boolean success = companyDAO.insertCompany(company);
            response.getWriter().write(success ? "Company created!" : "Failed to create company!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server Error!");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }
        
        User admin = (User) session.getAttribute("user");

        if (admin == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No company ID provided!");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(pathInfo.substring(1));
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No company ID provided!");
            return;
        }

        int companyId;
        try {
            companyId = Integer.parseInt(pathInfo.substring(1));
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
