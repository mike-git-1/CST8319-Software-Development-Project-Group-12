package com.inventra.servlet;

import com.inventra.database.dao.UserDAO;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.inventra.model.beans.User;
import com.inventra.service.SessionUtils;

import java.io.IOException;

@WebServlet(urlPatterns = { "/user/*" })
public class UserProfileServlet extends HttpServlet {
    private final UserDAO userDao = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (!SessionUtils.isUserLoggedIn(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        // session returns object. Cast to Integer, then int
        int userId = (Integer) session.getAttribute("userId");

        String lastName = request.getParameter("lastName");
        String firstName = request.getParameter("firstName");

        boolean updated = false;

        try {

            User existingUser = userDao.selectUser(userId);

            if (existingUser == null) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Error: User not found.\"}");
                return;
            }

            existingUser.setLast_name(lastName);
            existingUser.setFirst_name(firstName);
            updated = userDao.updateUser(existingUser);

            if (!updated) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter()
                        .write("{\"success\": false, \"message\": \"Error: Failed to update your personal info!\"}");
                return;

            }

            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter()
                    .write("{\"success\": true, \"message\": \"Your personal info was updated successfully!\"}");

        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Error: Invalid number format!\"}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter()
                    .write("{\"success\": false, \"message\": \"Error: Error updating your personal info!\"}");
        }
    }
}
