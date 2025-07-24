<%@ page import="java.util.List" %>
<%@ page import="yourpackage.models.Product" %>
<%
    List<Product> products = (List<Product>) request.getAttribute("products");
%>
<html>
<head><title>Inventory - Inventra</title></head>
<body>
<h2>Your Inventory</h2>

<table border="1">
    <tr>
        <th>Name</th><th>Description</th><th>Stock</th><th>Location</th><th>Actions</th>
    </tr>
<% for(Product p : products) { %>
    <tr>
        <td><%= p.getName() %></td>
        <td><%= p.getDescription() %></td>
        <td><%= p.getStockCount() %></td>
        <td><%= p.getLocation() %></td>
        <td>
            <form action="inventory" method="post" style="display:inline">
                <input type="hidden" name="action" value="delete" />
                <input type="hidden" name="id" value="<%= p.getId() %>" />
                <button type="submit">Delete</button>
            </form>
            
        </td>
    </tr>
<% } %>
</table>

<h3>Add New Product</h3>
<form action="inventory" method="post">
    <input type="hidden" name="action" value="add" />
    Name: <input type="text" name="name" required /><br/>
    Description: <input type="text" name="description" /><br/>
    Stock Count: <input type="number" name="stockCount" required /><br/>
    Location: <input type="text" name="location" /><br/>
    <button type="submit">Add Product</button>
</form>

<p><a href="dashboard.jsp">Back to Dashboard</a></p>
</body>
</html>
