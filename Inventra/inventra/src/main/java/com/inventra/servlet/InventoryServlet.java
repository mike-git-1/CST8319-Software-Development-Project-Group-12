package com.inventra.servlet;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.InventoryDAO;
import com.inventra.database.dao.LocationPermissionsDAO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Inventory;
import com.inventra.model.builders.InventoryBuilder;
import com.inventra.service.AuditService;
import com.inventra.service.SessionUtils;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/inventory/*" })
public class InventoryServlet extends HttpServlet {
    private final InventoryDAO inventoryDAO = new InventoryDAO();
    private final LocationPermissionsDAO locationPermissionDao = new LocationPermissionsDAO();
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

        int userId = (Integer) session.getAttribute("userId");

        try {
            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            request.setAttribute("inventories", inventoryDAO.getProductInventoryDTOByLocation(locationId));
            request.getRequestDispatcher("/inventory-list.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch locations");
        }

    }

    // Add or update inventory
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addInventory(request, response);
        } else if ("update".equals(action)) {
            updateInventory(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

    }

    private void addInventory(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));

            int locationId = locationPermissionDao.getLocationIdByUserId(userId);

            if (inventoryDAO.getInventoryByLocationAndProduct(locationId, productId) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Error: Product already exists in inventory.\"}");
                return;
            }

            Inventory inventory = new InventoryBuilder()
                    .withLocationId(locationId)
                    .withProductId(productId)
                    .withAmount(quantity)
                    .build();

            boolean success = inventoryDAO.insertInventory(inventory);

            if (!success) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to add product to inventory.\"}");
                return;
            }

            auditService.logNewInventory(userId, companyId, locationId, productId, inventory);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"success\": true, \"message\": \"Product added to inventory.\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"success\": false, \"message\": \"Server error while creating product.\"}");
        }
    }

    private void updateInventory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");
            int productId = Integer.parseInt(request.getParameter("productId"));

            int quantity = Integer.parseInt(request.getParameter("quantity"));

            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            Inventory existingInventory = inventoryDAO.getInventoryByLocationAndProduct(locationId, productId);

            if (existingInventory == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found in inventory!\"}");
                return;
            }

            boolean updated = inventoryDAO.updateInventoryAmount(locationId, productId, quantity);

            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product failed to update in inventory.\"}");

                return;
            }

            auditService.logUpdatedInventory(userId, companyId, locationId, productId, existingInventory.getAmount(),
                    quantity);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Product updated in inventory successfully!\"}");

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

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = request.getPathInfo(); // id Params from the url e.g "/12/13"

        // /12/13 becomes ["", "23", "45"]
        if (pathInfo == null || pathInfo.split("/").length < 3) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        String[] params = pathInfo.split("/"); // ["", "23", "45"]

        try {
            int locationId = Integer.parseInt(params[1]);
            int productId = Integer.parseInt(params[2]);

            int userId = (Integer) session.getAttribute("userId");
            int companyId = (Integer) session.getAttribute("companyId");

            Inventory existingInventory = inventoryDAO.getInventoryByLocationAndProduct(locationId, productId);

            if (existingInventory == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found in inventory!\"}");
                return;
            }

            boolean deleted = inventoryDAO.deleteInventory(locationId, productId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product failed to be deleted from inventory.\"}");
                return;
            }

            auditService.logDeletedInventory(userId, companyId, locationId, productId, existingInventory);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Product deleted from inventory successfully!\"}");

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
