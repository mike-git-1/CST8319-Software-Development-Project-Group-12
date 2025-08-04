package com.inventra.servlet;

import com.inventra.database.dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/product/delete/*" })
public class ProductDeleteServlet extends HttpServlet {
    private final ProductDAO productDAO = new ProductDAO();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
