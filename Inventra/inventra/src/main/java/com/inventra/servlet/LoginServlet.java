package com.inventra.servlet;

import java.io.IOException;

import com.inventra.database.dao.CompanyPermissionsDAO;
import com.inventra.database.dao.LocationPermissionsDAO;
import com.inventra.database.dao.UserDAO;

import com.inventra.model.beans.User;
import com.inventra.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private final CompanyPermissionsDAO companyPermissionsDao = new CompanyPermissionsDAO();
	private final LocationPermissionsDAO locationPermissionsDao = new LocationPermissionsDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String error = "Invalid username or password.";
		;

		if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
			error = "Email and password are required.";
			request.setAttribute("error", error);
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}

		try {
			UserDAO userDAO = new UserDAO();
			// find user
			User user = userDAO.findUserByEmail(email);

			// if user does not exist or not verified
			if (user == null || !user.isVerified()) {
				request.setAttribute("error", error);
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			// check password
			AuthService authService = new AuthService();
			if (!authService.validateLogin(password, user.getPassword(),
					user.getSalt())) {
				request.setAttribute("error", error);
				request.getRequestDispatcher("index.jsp").forward(request, response);
				return;
			}

			// set userId in session
			request.getSession().setAttribute("userId", user.getId());

			int companyId = companyPermissionsDao.getCompanyIdByUserId(user.getId());

			if (companyId > 0) {
				// set companyId in session
				request.getSession().setAttribute("companyId", companyId);
			}

			int locationId = locationPermissionsDao.getLocationIdByUserId(user.getId());

			if (locationId > 0) {
				// set locationId in session
				request.getSession().setAttribute("locationId", locationId);
			}

			response.sendRedirect("dashboard");

		} catch (

		Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "An internal error occurred. Please try again.");
			request.getRequestDispatcher("index.jsp").forward(request, response);

		}

	}

}
