package com.inventra.servlet;

import com.inventra.database.dao.InventoryDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Inventory;

import java.io.IOException;

@WebServlet(urlPatterns = { "/inventory/update/*" })
public class InventoryUpdateServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
}
