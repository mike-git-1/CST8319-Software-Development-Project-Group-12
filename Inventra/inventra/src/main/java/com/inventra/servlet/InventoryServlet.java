package com.inventra.servlet;

import com.inventra.database.dao.InventoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Inventory;
import com.inventra.model.builders.InventoryBuilder;

import java.io.IOException;

@WebServlet(urlPatterns = { "/inventory/*" })
public class InventoryServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
}
