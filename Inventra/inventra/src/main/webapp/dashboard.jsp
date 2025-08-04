<%@ page session="true" %> <%@ page import="com.inventra.model.beans.User" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %> <% User user =
(User) session.getAttribute("user"); if (user == null) {
response.sendRedirect("index"); } %> <%@ include file="products-list.jsp" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Inventra</title>
    <link rel="stylesheet" href="CSS/dashboard.css" />
    <script src="JS/dashboard.js" defer></script>
    <link
      href="https://fonts.googleapis.com/css2?family=Material+Symbols+Outlined"
      rel="stylesheet"
    />
  </head>
  <body>
    <div class="dashboard">
      <!-- header -->
      <header class="header">
        <div class="logo">Inventra</div>
        <div class="user-info">
          <span>Welcome, ${user.email}!</span>
          <div class="user-avatar">
            ${user.first_name.charAt(0)}${user.last_name.charAt(0)}
          </div>
        </div>
      </header>

      <!-- Sidebar -->
      <nav class="sidebar" id="sidebar">
        <div>
          <a
            href="#"
            class="nav-item active"
            onclick="showSection('products-section',this)"
            >Products</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('inventory-section',this)"
            >Inventory</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('analytics-section',this)"
            >Analytics</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('users-section',this)"
            >Users</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('locations-section',this)"
            >Locations</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('companies-section',this)"
            >Companies</a
          >
          <a
            href="#"
            class="nav-item"
            onclick="showSection('my-account-section',this)"
            >My Account</a
          >
        </div>
        <div class="sidebar-footer">
          <form action="/inventra/logout" method="GET">
            <button class="logout-btn" type="submit">
              Logout
              <span class="logout-icon material-symbols-outlined">
                logout
              </span>
            </button>
          </form>
        </div>
      </nav>

      <div class="toast" id="toast"></div>

      <main class="main">
        <!-- Products -->
        <div id="products-section">
          <div class="section-header">
            <h2>Products</h2>
            <button class="add-btn" onclick="showProductModal()">
              <span style="margin-right: 8px">+</span>Add Product
            </button>
          </div>
          <div class="table-container">
            <table class="table" id="products-table">
              <thead>
                <tr>
                  <th>SKU</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Price</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="products-table-body">
                <!-- Product rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Product modal form -->
        <div id="product-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="product-modal-title">Add New Product</h3>
              <span class="close" onclick="closeModal('product')">&times;</span>
            </div>
            <form id="product-form" action="#">
              <div class="form-group">
                <input type="hidden" id="product-id" name="product-id" />
              </div>
              <div class="form-group">
                <label for="product-name">Name</label>
                <input
                  type="text"
                  id="product-name"
                  name="product-name"
                  required
                />
              </div>
              <div class="form-group">
                <label for="product-price">Price ($)</label>
                <input
                  type="number"
                  id="product-price"
                  name="product-price"
                  step="0.01"
                  min="0"
                  required
                />
              </div>
              <div class="form-group">
                <label for="product-description">Description</label>
                <textarea
                  id="product-description"
                  name="product-description"
                  rows="2"
                  maxlength="90"
                  required
                  placeholder="Short description"
                ></textarea>
              </div>

              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('product')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="product-modal-btn">
                  Add
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- inventory -->
        <div id="inventory-section" style="display: none">
          <div class="section-header">
            <h2>Product Inventory</h2>
          </div>
          <div class="table-container">
            <table class="table" id="inventory-table">
              <thead>
                <tr>
                  <th>SKU</th>
                  <th>Name</th>
                  <th>Price</th>
                  <th>Quantity</th>
                  <th>Location</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="inventory-table-body">
                <!-- Inventory rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Inventory modal form -->
        <div id="inventory-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="inventory-modal-title">Add to Inventory</h3>
              <span class="close" onclick="closeModal('inventory')"
                >&times;</span
              >
            </div>
            <form id="inventory-form" action="#">
              <div class="form-group">
                <input
                  type="hidden"
                  id="inv-product-id"
                  name="inv-product-id"
                />
                <input
                  type="hidden"
                  id="inv-product-location-id"
                  name="inv-product-location-id"
                />
                <input type="hidden" id="inv-mode" name="inv-mode" />
              </div>
              <div class="form-group">
                <label for="inv-product-sku">SKU</label>
                <input
                  type="text"
                  id="inv-product-sku"
                  name="inv-product-sku"
                  value="[to be prepopulated from backend]"
                  disabled
                />
              </div>
              <div class="form-group">
                <label for="inv-product-name">Name</label>
                <input
                  type="text"
                  id="inv-product-name"
                  name="inv-product-name"
                  value="[to be prepopulated from backend]"
                  disabled
                />
              </div>
              <div class="form-group">
                <label for="inv-product-price">Price ($)</label>
                <input
                  type="text"
                  id="inv-product-price"
                  name="inv-product-price"
                  value="[to be prepopulated from backend]"
                  disabled
                />
              </div>
              <!-- <div class="form-group">
                <label for="inv-product-location">Location</label>
                <input
                  type="text"
                  id="inv-product-location"
                  name="inv-product-location"
                  required
                />
              </div> -->
              <div class="form-group">
                <label for="inv-product-qty">Quantity in Stock</label>
                <input
                  type="number"
                  id="inv-product-qty"
                  name="inv-product-qty"
                  step="1"
                  min="0"
                  required
                />
              </div>

              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('inventory')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="inv-modal-btn">
                  Add
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Analytics -->
        <div id="analytics-section" style="display: none">
          <div class="section-header">
            <h2>Analytics</h2>
          </div>
          <p>This is the analytics section</p>
        </div>

        <!-- Users -->
        <div id="users-section" style="display: none">
          <div class="section-header">
            <h2>User Management</h2>
            <button class="add-btn" onclick="showUserModal()">
              <span style="margin-right: 8px">+</span>Add User
            </button>
          </div>
          <div class="table-container">
            <table class="table" id="users-table">
              <thead>
                <tr>
                  <th>Avatar</th>
                  <th>LastName</th>
                  <th>First Name</th>
                  <th>Email</th>
                  <th>Location</th>
                  <!-- <th>Role</th> -->
                  <th>Status</th>
                  <th>Date created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="users-table-body">
                <!-- User rows will be added here  -->
                <!-- <tr>
                  <td><div class="avatar-table">JD</div></td>
                  <td>Doe</td>
                  <td>John</td>
                  <td>john.doe@example.com</td>
                  <td>Ottawa</td>
                  <td><span class="role-badge admin">Admin</span></td>
                  <td><span class="status-badge active">Verified</span></td>
                  <td>2 hours ago</td>
                  <td>
                    <button class="action-btn edit" onclick="editUser(this)">
                      Edit
                    </button>
                    <button
                      class="action-btn delete"
                      onclick="deleteRow(this,'user')"
                    >
                      Delete
                    </button>
                  </td>
                </tr> -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- User modal form -->
        <div id="user-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="user-modal-title">Add New User</h3>
              <span class="close" onclick="closeModal('user')">&times;</span>
            </div>
            <form id="user-form" action="#">
              <!-- <div class="form-row">
                <div class="form-group">
                  <label for="user-lastname">Last Name</label>
                  <input
                    type="text"
                    id="user-lastname"
                    name="user-lastname"
                    required
                  />
                </div>
                <div class="form-group">
                  <label for="user-firstname">First Name</label>
                  <input
                    type="text"
                    id="user-firstname"
                    name="user-firstname"
                    required
                  />
                </div>
              </div> -->
              <!-- <div class="form-group">
                <label for="user-location">Location</label>
                <select id="user-location" name="user-location" required>
                  <option style="color: #666" value="">Select Location</option>
                  <option value="1">Location 1</option>
                  <option value="2">Location 2</option>
                  <option value="3">Location 3</option>
                </select>
              </div> -->

              <div class="form-group">
                <label for="user-email">Email</label>
                <input type="email" id="user-email" name="email" required />
              </div>
              <div class="form-group">
                <p>An invitation link will be sent to this email.</p>
                <div class="invitation-link" id="invitation-link"></div>
              </div>
              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('user')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="user-modal-btn">
                  Send Link
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Locations -->
        <div id="locations-section" style="display: none">
          <div class="section-header">
            <h2>Locations</h2>
            <button class="add-btn" onclick="showLocationModal()">
              <span style="margin-right: 8px">+</span>Add Location
            </button>
          </div>
          <div class="table-container">
            <table class="table" id="locations-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Address 1</th>
                  <th>Address 2</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="locations-table-body">
                <!-- Location rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Location modal form -->
        <div id="location-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="location-modal-title">Add New Location</h3>
              <span class="close" onclick="closeModal('location')"
                >&times;</span
              >
            </div>
            <form id="location-form" action="#">
              <div class="form-group">
                <input type="hidden" id="location-id" name="location-id" />
              </div>
              <div class="form-group">
                <label for="location-name">Name</label>
                <input
                  type="text"
                  id="location-name"
                  name="location-name"
                  required
                />
              </div>
              <div class="form-group">
                <label for="location-address1">Address 1</label>
                <input
                  type="text"
                  id="location-address1"
                  name="location-address1"
                  required
                />
              </div>
              <div class="form-group">
                <label for="location-address2">Address 2</label>
                <input
                  type="text"
                  id="location-address2"
                  name="location-address2"
                />
              </div>
              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('location')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="location-modal-btn">
                  Add
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Companies -->
        <div id="companies-section" style="display: none">
          <div class="section-header">
            <h2>Companies</h2>
            <button class="add-btn" onclick="showCompanyModal()">
              <span style="margin-right: 8px">+</span>Add Company
            </button>
          </div>
          <div class="table-container">
            <table class="table" id="company-table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Date created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="company-table-body">
                <!-- Company rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Company modal form -->
        <div id="company-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="company-modal-title">Add New Company</h3>
              <span class="close" onclick="closeModal('company')">&times;</span>
            </div>
            <form id="company-form" action="#">
              <div class="form-group">
                <input type="hidden" id="company-id" name="company-id" />
              </div>
              <div class="form-group">
                <label for="company-name">Company Name</label>
                <input
                  type="text"
                  id="company-name"
                  name="company-name"
                  required
                />
              </div>
              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('company')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="company-modal-btn">
                  Add
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- My Account -->
        <div id="my-account-section" style="display: none">
          <div class="section-header">
            <h2>My Account</h2>
          </div>
          <form id="edit-my-account-form" action="#">
            <div class="form-group">
              <label for="user-lastname">Last Name</label>
              <input type="text" id="user-lastname" name="lastname" />
            </div>
            <div class="form-group">
              <label for="user-firstname">First Name</label>
              <input type="text" id="user-firstname" name="firstname" />
            </div>
            <div class="form-group">
              <label for="user-email">Email</label>
              <input
                type="email"
                id="user-email"
                name="email"
                value="[to be prepopulated from backend]"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="user-company">Company</label>
              <input
                type="text"
                id="user-company"
                name="company"
                value="[to be prepopulated from backend]"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="user-location">Location</label>
              <input
                type="text"
                id="user-location"
                name="location"
                value="[to be prepopulated from backend]"
                disabled
              />
            </div>
            <div class="form-actions">
              <button type="submit" class="add-btn">Save Changes</button>
            </div>
          </form>
        </div>
      </main>
    </div>
  </body>
</html>
