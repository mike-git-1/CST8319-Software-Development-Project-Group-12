package servlet;
import database.DBConnection;
import database.dao.ProductDAO;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Product;
import model.builders.ProductBuilder;
import util.SKUGenerator;
import java.io.IOException;
import java.sql.Connection;

@WebServlet(urlPatterns = {"/products/*"})
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain;charset=UTF-8");

        try {
            int companyId = Integer.parseInt(request.getParameter("company_id"));
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

            response.getWriter().write(success ? "Product created with SKU: " + sku : "Failed to create product!");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid number format!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error creating product!");
        }
    }
}
