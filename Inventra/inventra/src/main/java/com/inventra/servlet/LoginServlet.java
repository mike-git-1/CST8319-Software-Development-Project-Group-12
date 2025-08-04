package com.inventra.servlet;

import java.io.IOException;

import com.inventra.database.dao.CompanyDAO;
import com.inventra.database.dao.UserDAO;
import com.inventra.model.beans.Company;
import com.inventra.model.beans.User;
import com.inventra.service.AuthService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	public LoginServlet() {
		super();
	}

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

			// if does not exist or not verified
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

			CompanyDAO companyDAO = new CompanyDAO();
			Company company = companyDAO.getCompanyByUserId(user.getId());

			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("company", company);
			response.sendRedirect("dashboard");

		} catch (

		Exception e) {
			e.printStackTrace();
			request.setAttribute("error", "An internal error occurred. Please try again.");
			request.getRequestDispatcher("index.jsp").forward(request, response);

		}

	}

}

// private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
// throws Exception {
// String email = req.getParameter("email");
// String password = req.getParameter("password");

// UserDAO userDAO = new UserDAO(conn);
// User user = userDAO.findByEmail(email);

// if (user != null) {
// AuthService authService = new AuthService();
// if (authService.validateLogin(password, user.getHashedPassword(),
// user.getSalt())) {
// HttpSession session = req.getSession();
// session.setAttribute("user", user);
// resp.sendRedirect("dashboard.jsp");
// return;
// }
// }

// resp.sendRedirect("login.jsp?error=invalid");
// }