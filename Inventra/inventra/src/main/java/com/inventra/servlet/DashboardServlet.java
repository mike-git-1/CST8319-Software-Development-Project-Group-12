package com.inventra.servlet;

import java.io.IOException;

import com.inventra.database.dao.CompanyDAO;
import com.inventra.database.dao.LocationDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.database.dao.UserDAO;
import com.inventra.service.SessionUtils;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();
    private final LocationDAO locationDAO = new LocationDAO();
    private final LocationPermissionsDAO locationPermissionDao = new LocationPermissionsDAO();

    public DashboardServlet() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (!SessionUtils.isUserLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        int companyId = (Integer) session.getAttribute("companyId");

        try {
            int locationId = locationPermissionDao.getLocationIdByUserId(userId);
            request.setAttribute("user", userDao.selectUser(userId));
            request.setAttribute("company", companyDAO.getCompanyById(companyId));
            request.setAttribute("location", locationDAO.getLocationById(locationId));
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error while loading dashboard.");
        }

        RequestDispatcher rq = getServletConfig().getServletContext().getRequestDispatcher("/dashboard.jsp");
        rq.forward(request, response);
    }
}
