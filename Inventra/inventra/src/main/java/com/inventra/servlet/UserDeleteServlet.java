package com.inventra.servlet;

import com.inventra.database.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = { "/user/delete/*" })
public class UserDeleteServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo(); // e.g "/user/delete/1"
        if (pathInfo == null || pathInfo.length() <= 1) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No user ID provided!");
            return;
        }

        int userId;

        try {
            userId = Integer.parseInt(pathInfo.substring(1)); // get the user id from the url
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user ID!");
            return;
        }

        try {
            boolean deleted = userDAO.deleteUser(userId);

            if (deleted) {
                response.getWriter().write("User deleted successfully!");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to delete user");
        }

    }
}
