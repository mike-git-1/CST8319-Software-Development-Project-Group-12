package servlet;

import database.dao.UserDAO;
import model.beans.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/verify")
public class VerifyServlet extends HttpServlet {

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
        String token = req.getParameter("token");

        if (token == null || token.isEmpty()) {
            resp.getWriter().println("Invalid verification link!");
            return;
        }

        try {
            // Get user by verification token
            User user = userDAO.getUserByToken(token);

            if (user != null) {
                // Mark user as verified
                user.setVerified(true);
                user.setVerification_token(null); // clear token
                userDAO.updateUser(user);

                resp.getWriter().println("Email verified successfully! You can now log in.");
            } else {
                resp.getWriter().println("Invalid or expired verification link.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.getWriter().println("An error occurred while verifying email.");
        }
    }
}
