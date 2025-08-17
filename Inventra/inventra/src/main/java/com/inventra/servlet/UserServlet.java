package com.inventra.servlet;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.UserDAO;
import com.inventra.model.beans.Company;
import com.inventra.model.beans.CompanyPermission;
import com.inventra.model.beans.User;
import com.inventra.model.builders.CompanyPermissionBuilder;
import com.inventra.model.builders.UserBuilder;

@WebServlet(urlPatterns = { "/users/*" })
public class UserServlet extends HttpServlet {
        private final UserDAO userDao = new UserDAO();
        private final CompanyPermissionsDAO companyPermissionDao = new CompanyPermissionsDAO();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                HttpSession session = request.getSession(false);

                if (session == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
                return;
                }
                // session returns object. Cast to Company
                // Company company = (Company) session.getAttribute("company");
                // int companyId = company.getCompanyId();

                try {
                // request.setAttribute("users", userDAO.getUsersByCompanyId(companyId));
                request.setAttribute("users", userDao.selectAllUsers());
                request.getRequestDispatcher("/users-list.jsp").forward(request, response);
                } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to fetch users");
                }
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                        throws ServletException, IOException {

                resp.setContentType("application/json");
                resp.setCharacterEncoding("UTF-8");

                HttpSession session = req.getSession(false);

                if (session == null) {
                        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized - User not logged in!");
                        return;
                }
                // session returns object. Cast to Company
                Company company = (Company) session.getAttribute("company");
                int companyId = company.getCompanyId();
                String email = req.getParameter("email");
                String token = UUID.randomUUID().toString();

                try {
                        if (userDao.isEmailTaken(email)) {
                                resp.getWriter().write(String.format(
                                                "{\"success\": false, \"message\": \"This email is is already taken.\"}"));
                                return;
                        }

                        User user = new UserBuilder()
                                        .setFirstName("--")
                                        .setLastName("--")
                                        .setEmail(email)
                                        .setPassword("TEMP")
                                        .setSalt("TEMP-SALT")
                                        .setVerificationToken(token)
                                        .setVerified(false)
                                        .build();

                        boolean user_success = userDao.insertUser(user);

                        if (!user_success) {
                                resp.getWriter().write(
                                                "{\"success\": false, \"message\": \"Failed to send invitation. User could not be created.\"}");
                                return;
                        }

                        User newUser = userDao.findUserByEmail(email);

                        if (newUser == null) {
                                resp.getWriter().write(
                                                "{\"success\": false, \"message\": \"Failed to send invitation. User could not be found.\"}");
                                return;
                        }

                        CompanyPermission companyPermission = new CompanyPermissionBuilder()
                                        .withCompanyId(companyId)
                                        .withUserId(newUser.getId())
                                        .build();

                        boolean companyPermission_success = companyPermissionDao
                                        .insertCompanyPermission(companyPermission);

                        if (companyPermission_success) {
                                resp.getWriter().write(String.format(
                                                "{\"success\": true, \"link\": \"http://localhost:8080/inventra/auth?token=%s\", \"message\": \"An invite has been sent to %s!\"}",
                                                token, email));
                        } else {
                                resp.getWriter().write(
                                                "{\"success\": false, \"message\": \"Failed to send invitation.\"}");
                        }

                } catch (Exception e) {
                        e.printStackTrace();
                        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        resp.getWriter().write("{\"success\": false, \"message\": \"Server error occurred.\"}");
                }

        }

        @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
                // 
        }

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
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
                boolean deleted = userDao.deleteUser(userId);

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
