package com.inventra.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.CompanyDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Company;
import com.inventra.model.builders.CompanyBuilder;
import com.inventra.service.AuditService;
import com.inventra.service.SessionUtils;

@WebServlet(urlPatterns = { "/companies/*" })
public class CompanyServlet extends HttpServlet {
    private final CompanyDAO companyDAO = new CompanyDAO();
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
            request.setAttribute("company", companyDAO.getCompanyById(companyId));
            request.getRequestDispatcher("/companies-list.jsp").forward(request, response);
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
            addCompany(request, response);
        } else if ("update".equals(action)) {
            updateCompany(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }
    }

    private void addCompany(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            // session returns object. Cast to Integer, then int
            int adminId = (Integer) session.getAttribute("userId");

            String name = request.getParameter("name");

            Company company = new CompanyBuilder()
                    .withName(name)
                    .withAdminId(adminId)
                    .withDateCreated(new Date())
                    .build();

            boolean success = companyDAO.insertCompany(company);

            if (!success) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to create company.\"}");
                return;
            }
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Company added successfully.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error during company creation.\"}");
        }
    }

    private void updateCompany(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String pathInfo = request.getPathInfo(); // id Param from the url
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {
            // session returns object. Cast to Integer, then int
            int adminId = (Integer) session.getAttribute("userId");
            int adminCompanyId = (Integer) session.getAttribute("companyId");

            int companyId = Integer.parseInt(pathInfo.substring(1)); // get the company id from the url

            String name = request.getParameter("name");

            Company existingCompany = companyDAO.getCompanyById(companyId);

            if (existingCompany == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"success\": false, \"message\": \"Error: Company not found.\"}");
                return;
            }

            String currentName = existingCompany.getName();

            existingCompany.setName(name);
            existingCompany.setAdminId(adminId);

            boolean updated = companyDAO.updateCompany(existingCompany);

            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to update company.\"}");

                return;
            }

            auditService.logUpdatedCompany(adminId, adminCompanyId, companyId, currentName, name);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Company updated successfully!\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while updating company.\"}");

        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {
            int companyId = Integer.parseInt(pathInfo.substring(1));

            boolean deleted = companyDAO.deleteCompany(companyId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to delete company.\"}");
                return;
            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Company deleted successfully.\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete company");
        }

    }

}
