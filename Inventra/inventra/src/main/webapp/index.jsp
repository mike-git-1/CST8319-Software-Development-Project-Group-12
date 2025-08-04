<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Inventra</title>
    <link rel="stylesheet" href="CSS/style.css" />
    <link
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
      rel="stylesheet"
    />

    <script src="JS/script.js" defer></script>
  </head>
  <body>
    <!-- Navigation -->
    <nav class="navbar" id="navbar">
      <div class="nav-container">
        <div class="logo">Inventra</div>
        <ul class="nav-menu" id="navMenu">
          <li><a href="#" class="nav-link">Home</a></li>
          <li><a href="#" class="nav-link">Features</a></li>
          <li><a href="#" class="nav-link">About</a></li>
          <li><a href="#" class="nav-link">Contact</a></li>
        </ul>

        <div class="hamburger" id="hamburger">
          <span class="material-symbols-outlined">menu</span>
          <span class="material-symbols-outlined">close</span>
        </div>
      </div>
    </nav>

    <div class="blur-bg-overlay"></div>
    <div class="form-popup">
      <span class="close-btn material-symbols-outlined">close</span>

      <!-- LOGIN -->
      <div class="form-box login">
        <div class="form-details">
          <h2>Welcome Back!</h2>
          <p>
            Please sign in with your login details<br />
            to get started.
          </p>
          <p>"Effortless inventory starts here"</p>
        </div>
        <div class="form-content">
          <h2>LOGIN</h2>
          <form action="login" method="post" id="login-form">
            <div class="input-field">
              <input type="email" id="email" name="email" required />
              <label for="email">Email</label>
            </div>
            <div class="input-field">
              <input type="password" id="pass" name="password" required />
              <label for="password">Password</label>

              <!-- If error parameter exists, toggle on the show-popup class-->
              <% String error = (String) request.getAttribute("error"); if
              (error != null) { %>
              <div class="login-error"><span>&#9888; <%= error %></span></div>
              <script>
                document.body.classList.add("show-popup");
              </script>
              <%}%>
            </div>
            <a href="#" class="forgot-pass">Forgot password?</a>
            <button type="submit">Log In</button>
          </form>
        </div>
      </div>
    </div>

    <!-- Hero Section -->
    <section class="hero" id="home">
      <div class="hero-content">
        <h1>Welcome to Inventra</h1>
        <p>
          Transform your business with modern inventory management tools <br />
          and real-time analytics all at your fingertips.
        </p>
        <button class="login-btn">Login</button>
      </div>
    </section>

    <!-- Footer -->
    <footer class="footer" id="contact">
      <div class="container">
        <p>&copy; 2025 Inventra. All rights reserved. | Privacy Policy</p>
      </div>
    </footer>
  </body>
</html>
