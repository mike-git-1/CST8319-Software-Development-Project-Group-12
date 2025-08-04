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

@WebServlet(urlPatterns = { "/products/*" })
public class ProductServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No product ID provided!");
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid product ID format!");
            return;
        }

        try {
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Product not found!");
                return;
            }

            request.setAttribute("product", product);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/product.jsp"); // TODO: Create product.jsp
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error!");
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
}
