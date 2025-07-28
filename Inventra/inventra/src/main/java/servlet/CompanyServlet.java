package servlet;
import java.io.IOException;
import java.util.Date;
import database.dao.CompanyDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.beans.Company;
import model.builders.CompanyBuilder;

@WebServlet(urlPatterns = {"/companies/*"})
public class CompanyServlet extends HttpServlet {
    private final CompanyDAO companyDAO = new CompanyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No company ID provided!");
            return;
        }

        String idStr = pathInfo.substring(1);
        int companyId;
        try {
            companyId = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid company ID format!");
            return;
        }

        try {
            Company company = companyDAO.getCompanyById(companyId);
            if (company == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Company not found!");
                return;
            }

            request.setAttribute("company", company);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/company.jsp"); // TODO: Create company/organization .jsp
            dispatcher.forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error!");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        int adminId = Integer.parseInt(request.getParameter("admin_id"));

        Company company = new CompanyBuilder()
                .withName(name)
                .withAdminId(adminId)
                .withDateCreated(new Date())
                .build();

        try {
            boolean success = companyDAO.insertCompany(company);
            response.getWriter().write(success ? "Company created!" : "Failed to create company!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server Error!");
        }
    }
}
