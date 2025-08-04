package com.inventra.servlet;

import java.io.IOException;
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

    }

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
}
