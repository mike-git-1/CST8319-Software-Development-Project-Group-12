<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Inventra</title>
    <link rel="stylesheet" href="CSS/signup.css" />
    <link
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
      rel="stylesheet"
    />

    <script src="JS/signup.js" defer></script>
  </head>
  <body>
    <div class="form-container">
      <div class="form-header">
        <h1>Inventra</h1>
        <h2>Create Account</h2>
        <p>Enter your details below to finish <br />creating your account.</p>
      </div>

      <div class="success-message" id="successMessage">
        Account created successfully! Welcome to Inventra!
      </div>

      <form id="signupForm" action="auth" method="post">
        <input type="hidden" name="token" value="${user.verification_token}" />
        <div class="form-row">
          <div class="form-group">
            <label for="firstName">First Name</label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              placeholder="John"
              required
            />
          </div>
          <div class="form-group">
            <label for="lastName">Last Name</label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              placeholder="Doe"
              required
            />
          </div>
        </div>

        <div class="form-group">
          <label for="email">Email</label>
          <input
            type="email"
            id="email"
            name="email"
            placeholder="john.doe@example.com"
            value="${user.email}"
            readonly
            disabled
          />
        </div>

        <div class="form-group">
          <label for="password">Password</label>
          <input
            type="password"
            id="password"
            name="password"
            placeholder="Enter your password"
            required
          />
        </div>

        <!-- If error parameter exists-->
        <% String error = (String) request.getAttribute("error"); if (error !=
        null) { %>
        <div class="login-error"><span>&#9888; <%= error %></span></div>
        <%}%>

        <button type="submit" class="submit-btn">Create Account</button>
      </form>

      <div class="signin-link">
        Already have an account?
        <a
          href="http://localhost:8080/inventra/index?page=login"
          id="signinLink"
          >Sign in</a
        >
      </div>
    </div>
  </body>
</html>
