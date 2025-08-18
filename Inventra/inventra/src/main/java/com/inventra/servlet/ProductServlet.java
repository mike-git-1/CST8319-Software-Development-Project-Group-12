package com.inventra.servlet;

import com.inventra.database.dao.AuditDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.database.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Product;
import com.inventra.model.builders.ProductBuilder;
import com.inventra.service.AuditService;
import com.inventra.service.SessionUtils;
import com.inventra.util.SKUGenerator;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/products/*" })
public class ProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();
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

        int companyId = (Integer) session.getAttribute("companyId");

        try {
            request.setAttribute("products", productDAO.getProductsByCompanyId(companyId));
            request.getRequestDispatcher("/products-list.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch products");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("add".equals(action)) {
            addProduct(request, response);
        } else if ("update".equals(action)) {
            updateProduct(request, response);
        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }
    }

    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int userId = (Integer) session.getAttribute("userId");

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));

            String sku = SKUGenerator.generateUniqueSKU();

            Product product = new ProductBuilder()
                    .withCompanyId(companyId)
                    .withName(name)
                    .withSku(sku)
                    .withDescription(description)
                    .withPrice(price)
                    .build();
            int newProductId = productDAO.insertProductAndReturnId(product);

            if (newProductId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write(
                        "{\"success\": false, \"message\": \"Failed to create product!\"}");
                return;
            }
            auditService.logNewProduct(userId, companyId, newProductId, product);

            response.setStatus(HttpServletResponse.SC_CREATED);
            response.getWriter().write(
                    String.format(
                            "{\"success\": true, \"message\": \"Product created successfully!\", \"sku\": \"%s\"}",
                            sku));

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while adding product.\"}");

        }
    }

    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {

            int companyId = (Integer) session.getAttribute("companyId");
            int userId = (Integer) session.getAttribute("userId");

            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            if (locationId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Could not find user's location.\"}");
                return;
            }

            int productId = Integer.parseInt(pathInfo.substring(1)); // get the product id from the url

            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));

            Product existingProduct = productDAO.getProductById(productId);

            if (existingProduct == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found!\"}");
                return;

            }

            Product updatedProduct = new ProductBuilder()
                    .withProductId(productId)
                    .withName(name)
                    .withSku(existingProduct.getSku())
                    .withDescription(description)
                    .withPrice(price)
                    .build();

            // updatedProduct.setName(name);
            // updatedProduct.setDescription(description);
            // updatedProduct.setPrice(price);

            boolean updated = productDAO.updateProduct(updatedProduct);

            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product failed to update.\"}");

                return;
            }

            auditService.logUpdatedProduct(userId, companyId, locationId, productId, existingProduct, updatedProduct);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Product updated successfully!\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Server error while updating user.\"}");

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

        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Error: Something went wrong.\"}");
            return;
        }

        try {
            int companyId = (Integer) session.getAttribute("companyId");
            int userId = (Integer) session.getAttribute("userId");

            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            if (locationId < 0) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"success\": false, \"message\": \"Could not find user's location.\"}");
                return;
            }
            int productId = Integer.parseInt(pathInfo.substring(1)); // get the product id from the url

            Product productToDelete = productDAO.getProductById(productId);

            if (productToDelete == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Product not found!\"}");
                return;
            }
            boolean deleted = productDAO.deleteProduct(productId);

            if (!deleted) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Failed to delete product.\"}");
                return;
            }

            auditService.logDeletedProduct(userId, companyId, locationId, productId,
                    productToDelete);

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Product deleted successfully!\"}");
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"success\": false, \"message\": \"Invalid number format.\"}");

        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete product");
        }
    }
}
