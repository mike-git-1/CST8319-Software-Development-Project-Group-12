package com.inventra.servlet;

import com.inventra.database.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import com.inventra.model.beans.Product;
import java.io.IOException;

@WebServlet(urlPatterns = { "/product/update/*" })
public class ProductUpdateServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
}
