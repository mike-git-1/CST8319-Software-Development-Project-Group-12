<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Login - Inventra</title></head>
<body>
<h2>Login</h2>
<form action="auth" method="post">
    <input type="hidden" name="action" value="login" />
    Email: <input type="email" name="email" required /><br/>
    Password: <input type="password" name="password" required /><br/>
    <button type="submit">Login</button>
</form>
<% String error = request.getParameter("error");
   if ("invalid".equals(error)) { %>
    <p style="color:red;">Invalid credentials, please try again.</p>
<% } %>
<p>Don't have an account? Contact your company admin.</p>
</body>
</html>
