package servlet;

import database.dao.UserDAO;
import model.beans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class UserServlet extends HttpServlet {

    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        try {
            userDAO = new UserDAO();
        } catch (SQLException e) {
            throw new ServletException("Unable to initialize UserDAO", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Show login page
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Handle login form submission
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        // TODO: You should hash the password here before checking!
        // For demo, assuming plain text matching in UserDAO.validateLogin
        User user = userDAO.validateLogin(email, password);

        if (user != null) {
            // Login success
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect("dashboard.jsp");  // Redirect to dashboard or home page
        } else {
            // Login failed
            req.setAttribute("error", "Invalid email or password.");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}

