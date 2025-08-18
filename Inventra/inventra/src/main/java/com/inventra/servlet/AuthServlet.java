package com.inventra.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

import com.inventra.database.dao.UserDAO;
import com.inventra.model.beans.User;
import com.inventra.service.AuthService;

@WebServlet("/auth/*")
public class AuthServlet extends HttpServlet {

    private final UserDAO userDao = new UserDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String token = request.getParameter("token");

        if (token == null) {
            response.sendRedirect(request.getContextPath() + "/index");
            return;
        }

        try {

            User user = userDao.findUserByToken(token);
            if (user != null) {
                request.setAttribute("user", user);
                request.getRequestDispatcher("signup.jsp").forward(request, response);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.sendRedirect(request.getContextPath() + "/index");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Registration handler
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String first_name = req.getParameter("firstName");
        String last_name = req.getParameter("lastName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String token = req.getParameter("token");
        String infoMsg = null;

        try {
            if (userDao.isEmailTaken(email)) {
                infoMsg = "This email is already taken.";
                req.setAttribute("error", infoMsg);
                req.getRequestDispatcher("signup.jsp").forward(req, resp);
                return;
            }

            String salt = AuthService.generateSalt();
            String hashedPassword = AuthService.hashPassword(password, salt);
            System.out.println("Salt: " + salt);
            System.out.println("Hash: " + hashedPassword);

            User user = userDao.findUserByToken(token);

            if (user == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found!");
                return;
            }

            user.setFirst_name(first_name);
            user.setLast_name(last_name);
            user.setPassword(hashedPassword);
            user.setSalt(salt);

            boolean updated = userDao.updateUser(user);
            boolean verified = userDao.verifyUserByToken(token);

            if (!updated || !verified) {
                infoMsg = "Account verification failed. Please contact support or try again.";
                req.setAttribute("error", infoMsg);
                req.setAttribute("user", user);
                req.getRequestDispatcher("signup.jsp").forward(req, resp);
                return;
            }

            resp.sendRedirect("signupConfirmed");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
