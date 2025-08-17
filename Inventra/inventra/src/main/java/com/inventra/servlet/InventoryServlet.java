package com.inventra.servlet;

import com.inventra.database.dao.InventoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Company;
import com.inventra.model.beans.Inventory;
import com.inventra.model.builders.InventoryBuilder;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/inventory/*" })
public class InventoryServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch locations");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            // int locationId = Integer.parseInt(request.getParameter("locationId"));
            int locationId = 111; // temp
            int productId = Integer.parseInt(request.getParameter("productId"));

            if (inventoryDAO.getInventoryByLocationAndProduct(locationId, productId) != null) {
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Error: Product already exists in inventory.\"}");
                return;
            }

            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Inventory inventory = new InventoryBuilder()
                    .withLocationId(locationId)
                    .withProductId(productId)
                    .withAmount(quantity)
                    .build();

            boolean success = inventoryDAO.insertInventory(inventory);

            if (success) {
                response.getWriter().write("{\"success\": true, \"message\": \"Product added to inventory.\"}");
            } else {
                response.getWriter().write("{\"success\": false, \"message\": \"Failed to create product.\"}");
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating product.\"}");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int locationId = Integer.parseInt(request.getParameter("locationId"));
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            Inventory existingInventory = inventoryDAO.getInventoryByLocationAndProduct(locationId, productId);

            if (existingInventory == null) {
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found in inventory!\"}");
                return;
            }

            boolean updated = inventoryDAO.updateInventoryAmount(locationId, productId, quantity);

            if (updated) {
                response.getWriter()
                        .write("{\"success\": true, \"message\": \"Product updated in inventory successfully!\"}");
                return;
            } else {
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product failed to update in inventory.\"}");
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while updating invenetory.\"}");

        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            int locationId = Integer.parseInt(request.getParameter("locationId"));
            int productId = Integer.parseInt(request.getParameter("productId"));

            Inventory existingInventory = inventoryDAO.getInventoryByLocationAndProduct(locationId, productId);

            if (existingInventory == null) {
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found in inventory!\"}");
                return;
            }

            boolean deleted = inventoryDAO.deleteInventory(locationId, productId);

            if (deleted) {
                response.getWriter()
                        .write("{\"success\": true, \"message\": \"Product deleted from inventory successfully!\"}");
                return;
            } else {
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product failed to be deleted from inventory.\"}");
                return;
            }
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while deleting invenetory.\"}");
        }
    }
}
