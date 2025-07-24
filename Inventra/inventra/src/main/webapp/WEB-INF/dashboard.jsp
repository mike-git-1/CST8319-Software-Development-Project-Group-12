<%@ page import="yourpackage.models.User" %>
<%@ page session="true" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<html>
<head><title>Dashboard - Inventra</title></head>
<body>
<h2>Welcome, <%= user.getEmail() %>!</h2>
<p>Role: <%= user.getRole() %></p>
<a href="inventory">Manage Inventory</a> | 
<a href="logout.jsp">Logout</a>
</body>
</html>
