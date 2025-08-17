package com.inventra.servlet;

import com.inventra.database.DBConnection;
import com.inventra.database.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.Company;
import com.inventra.model.beans.Product;
import com.inventra.model.builders.ProductBuilder;
import com.inventra.util.SKUGenerator;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/products/*" })
public class ProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Usernot logged in!");
            return;
        }
        Company company = (Company) session.getAttribute("company");
        int companyId = company.getCompanyId();

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
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - Usernot logged in!");
            return;
        }
        // session returns object. Cast to Company
        Company company = (Company) session.getAttribute("company");
        int companyId = company.getCompanyId();

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));

            String sku;
            try (Connection conn = DBConnection.getConnectionToDatabase()) {
                sku = SKUGenerator.generateUniqueSKU();
            }

            Product product = new ProductBuilder()
                    .withCompanyId(companyId)
                    .withName(name)
                    .withSku(sku)
                    .withDescription(description)
                    .withPrice(price)
                    .build();
            boolean success = productDAO.insertProduct(product);

            if (success) {
                response.getWriter().write(
                        String.format(
                                "{\"success\": true, \"message\": \"Product created successfully!\", \"sku\": \"%s\"}",
                                sku));
            } else {
                response.getWriter().write(
                        "{\"success\": false, \"message\": \"Failed to create product!\"}");
            }

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

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // id part of the url, e.g "/product/update/123"

        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No product ID provided!");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(pathInfo.substring(1)); // get the product id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID!");
            return;
        }

        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));

            Product existingProduct = productDAO.getProductById(productId);

            if (existingProduct == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found!");
                return;
            }

            existingProduct.setName(name);
            existingProduct.setDescription(description);
            existingProduct.setPrice(price);

            boolean updated = productDAO.updateProduct(existingProduct);

            if (updated) {
                response.setContentType("text/plain;charset=UTF-8");
                response.getWriter().write("Product updated successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update product!");
            }
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format!");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error updating product!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // e.g "/product/delete/1"
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No product ID provided!");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(pathInfo.substring(1)); // get the product id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID!");
            return;
        }

        try {
            boolean deleted = productDAO.deleteProduct(productId);

            if (deleted) {
                response.getWriter().write("Product deleted successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete product");
        }
    }
}
