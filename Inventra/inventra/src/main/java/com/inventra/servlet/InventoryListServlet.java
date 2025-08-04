package com.inventra.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

import com.inventra.database.dao.InventoryDAO;
import com.inventra.model.beans.Company;

@WebServlet(urlPatterns = { "/inventory-list" })
public class InventoryListServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
            return;
        }

        // session returns object. Cast to Company
        Company company = (Company) session.getAttribute("company");
        int companyId = company.getCompanyId();

        try {
            request.setAttribute("inventories", inventoryDAO.getProductInventoryDTOByCompany(companyId));
            request.getRequestDispatcher("/inventory-list.jsp").forward(request, response);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch locations");
        }

    }
}
