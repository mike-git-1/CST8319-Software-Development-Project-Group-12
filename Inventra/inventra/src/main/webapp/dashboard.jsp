<%@ page session="true" %> <%@ page contentType="text/html;charset=UTF-8"
language="java" %>
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
            id="products-link"
            onclick="showSection('products-section',this)"
            >Products</a
          >
          <a
            href="#"
            class="nav-item"
            id="inventory-link"
            onclick="showSection('inventory-section',this)"
            >Inventory</a
          >
          <a
            href="#"
            class="nav-item"
            id="audit-link"
            onclick="showSection('audit-section',this)"
            >Audit</a
          >
          <a
            href="#"
            class="nav-item"
            id="users-link"
            onclick="showSection('users-section',this)"
            >Users</a
          >
          <a
            href="#"
            class="nav-item"
            id="location-link"
            onclick="showSection('locations-section',this)"
            >Locations</a
          >
          <a
            href="#"
            class="nav-item"
            id="company-link"
            onclick="showSection('companies-section',this)"
            >Company Info</a
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
            <div class="section-title">
              <h2>Products</h2>
              <p class="section-subtitle">${company.name}</p>
            </div>
            <button
              class="add-btn"
              id="add-product-btn"
              onclick="showProductModal()"
            >
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
            <div class="section-title">
              <h2>Product Inventory</h2>
              <p class="section-subtitle">${company.name} • ${location.name}</p>
            </div>
          </div>
          <div class="table-container">
            <table class="table" id="inventory-table">
              <thead>
                <tr>
                  <th>SKU</th>
                  <th>Name</th>
                  <th>Location</th>
                  <th>Price</th>
                  <th>Quantity</th>
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
                <input type="hidden" id="inv-action" name="inv-action" />
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

        <!-- Audit-->
        <div id="audit-section" style="display: none">
          <div class="section-header">
            <div class="section-title">
              <h2>Audit</h2>
              <p class="section-subtitle">${company.name}</p>
            </div>
          </div>
          <div class="table-container">
            <table class="table" id="audit-table">
              <thead>
                <tr>
                  <th>Audit ID</th>
                  <th>Date</th>
                  <th>Location</th>
                  <th>User</th>
                  <th>Action</th>
                  <th>Target ID</th>
                  <th>Category</th>
                  <th>Property</th>
                  <th>Previous Value</th>
                  <th>New Value</th>
                </tr>
              </thead>
              <tbody id="audit-table-body">
                <!-- audit rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Users -->
        <div id="users-section" style="display: none">
          <div class="section-header">
            <div class="section-title">
              <h2>User Management</h2>
              <p class="section-subtitle">${company.name}</p>
            </div>
            <button class="add-btn" id="add-user-btn" onclick="showUserModal()">
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
                  <th>Status</th>
                  <th>Date created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody id="users-table-body">
                <!-- User rows will be added here  -->
              </tbody>
            </table>
          </div>
        </div>

        <!-- Add User modal form -->
        <div id="user-modal" class="modal">
          <div class="modal-content">
            <div class="modal-header">
              <h3 id="user-modal-title">Add New User</h3>
              <span class="close" onclick="closeModal('user')">&times;</span>
            </div>
            <form id="user-form" action="#">
              <div class="form-group">
                <label for="user-company">Company</label>
                <input
                  type="text"
                  id="user-company"
                  name="user-company"
                  value="${company.name}"
                  readonly
                  disabled
                />
              </div>
              <div class="form-group">
                <label for="edit-user-location">Location</label>
                <select id="add-user-location" name="user-location" required>
                  <!-- insert location options -->
                </select>
              </div>
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
                  Invite User
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Edit User modal form -->
        <div id="edit-user-modal" class="modal">
          <div class="modal-content" style="max-width: 700px">
            <div class="modal-header">
              <h3 id="user-modal-title">Edit User</h3>
              <span class="close" onclick="closeModal('edit-user')"
                >&times;</span
              >
            </div>
            <form id="edit-user-form" action="#">
              <div class="form-row">
                <div class="form-group">
                  <label for="edit-user-lastname">Last Name</label>
                  <input
                    type="text"
                    id="edit-user-lastname"
                    name="edit-user-lastname"
                  />
                </div>
                <div class="form-group">
                  <label for="edit-user-firstname">First Name</label>
                  <input
                    type="text"
                    id="edit-user-firstname"
                    name="edit-user-firstname"
                  />
                </div>
                <input type="hidden" id="edit-user-id" name="edit-user-id" />
              </div>
              <div class="form-group">
                <label for="edit-user-email">Email</label>
                <input type="email" id="edit-user-email" name="email" />
              </div>
              <div class="form-group">
                <label for="edit-user-location">Location</label>
                <select id="edit-user-location" name="user-location" required>
                  <!-- insert location options -->
                </select>
              </div>
              <div class="form-group">
                <label for="edit-user-hierarchy">Role</label>
                <select
                  id="edit-user-hierarchy"
                  name="edit-user-hierarchy"
                  required
                >
                  <option value="1">Admin</option>
                  <option value="99">Manager</option>
                  <option value="999">Employee</option>
                </select>
              </div>
              <div class="form-group" id="cmpy-perm-group">
                <p class="chkbox-subtitle">Company-level Access</p>
                <div class="checkbox-group">
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-view-audit"
                      name="permissions"
                      value="cmpy-view-audit"
                    />
                    <label for="cmpy-view-audit"> View Audit </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-add-rmv-user"
                      name="permissions"
                      value="cmpy-add-rmv-user"
                    />
                    <label for="cmpy-add-rmv-user"> Add/Remove Users </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-perm-manager"
                      name="permissions"
                      value="cmpy-perm-manager"
                    />
                    <label for="cmpy-perm-manager"> Permisison Manager </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-edit-name"
                      name="permissions"
                      value="cmpy-edit-name"
                    />
                    <label for="cmpy-edit-name">
                      Manage Companies & Locations
                    </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-add-rmv-products"
                      name="permissions"
                      value="cmpy-add-rmv-products"
                    />
                    <label for="cmpy-add-rmv-products">
                      Add/Remove Products
                    </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="cmpy-edit-products"
                      name="permissions"
                      value="cmpy-edit-products"
                    />
                    <label for="cmpy-edit-products"> Edit Products </label>
                  </div>
                </div>
              </div>
              <div class="form-group" id="loc-perm-group">
                <p class="chkbox-subtitle">Location-level Access</p>
                <div class="checkbox-group">
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-view-audit"
                      name="permissions"
                      value="loc-view-audit"
                    />
                    <label for="loc-view-audit"> View Audit </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-add-user"
                      name="permissions"
                      value="loc-add-user"
                    />
                    <label for="loc-add-user"> Add Users </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-rmv-user"
                      name="permissions"
                      value="loc-rmv-user"
                    />
                    <label for="loc-rmv-user"> Remove Users </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-perm-manager"
                      name="permissions"
                      value="loc-perm-manager"
                    />
                    <label for="loc-perm-manager"> Permisison Manager </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-edit-name"
                      name="permissions"
                      value="loc-edit-name"
                    />
                    <label for="loc-edit-name"> Edit Location Name </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-edit-addresses"
                      name="permissions"
                      value="loc-edit-addresses"
                    />
                    <label for="loc-edit-addresses"> Edit Addresses </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-view-stock"
                      name="permissions"
                      value="loc-view-stock"
                    />
                    <label for="loc-view-stock"> View Stock </label>
                  </div>
                  <div class="checkbox-item">
                    <input
                      type="checkbox"
                      id="loc-edit-stock"
                      name="permissions"
                      value="loc-edit-stock"
                    />
                    <label for="loc-edit-stock"> Manage Stock </label>
                  </div>
                </div>
              </div>

              <div class="form-actions">
                <button
                  type="button"
                  class="cancel-btn"
                  onclick="closeModal('edit-user')"
                >
                  Cancel
                </button>
                <button type="submit" class="add-btn" id="user-modal-btn">
                  Update
                </button>
              </div>
            </form>
          </div>
        </div>

        <!-- Locations -->
        <div id="locations-section" style="display: none">
          <div class="section-header">
            <div class="section-title">
              <h2>Locations</h2>
              <p class="section-subtitle">${company.name}</p>
            </div>
            <button
              class="add-btn"
              id="add-location-btn"
              onclick="showLocationModal()"
            >
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
            <h2>Company Info</h2>
            <button
              style="display: none"
              class="add-btn"
              onclick="showCompanyModal()"
            >
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
          <form id="my-account-form" action="#">
            <div class="form-group">
              <label for="myuser-lastname">Last Name</label>
              <input
                type="text"
                id="myuser-lastname"
                name="lastname"
                value="${user.last_name}"
              />
            </div>
            <div class="form-group">
              <label for="myuser-firstname">First Name</label>
              <input
                type="text"
                id="myuser-firstname"
                name="firstname"
                value="${user.first_name}"
              />
            </div>
            <div class="form-group">
              <label for="user-email">Email</label>
              <input
                type="email"
                id="user-email"
                name="email"
                value="${user.email}"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="user-company">Company</label>
              <input
                type="text"
                id="user-company"
                name="company"
                value="${company.name}"
                disabled
              />
            </div>
            <div class="form-group">
              <label for="user-location">Location</label>
              <input
                type="text"
                id="user-location"
                name="location"
                value="${location.name}"
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
