const loadProducts = async () => {
  console.log("loadProducts called");
  try {
    const response = await fetch("/inventra/products/");

    if (!response.ok) {
      throw new Error("Failed to load products");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("products-table-body").innerHTML = html; // add products list to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "products-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading products...</td></tr>`;
  }
};

const loadAuditTable = async () => {
  console.log("loadAuditTable called");
  try {
    const response = await fetch("/inventra/audit/");

    if (!response.ok) {
      throw new Error("Failed to load audit table");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("audit-table-body").innerHTML = html; // add audit logs to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "audit-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading audit logs...</td></tr>`;
  }
};

const loadInventory = async () => {
  console.log("loadInventory called");
  try {
    const response = await fetch("/inventra/inventory/");

    if (!response.ok) {
      throw new Error("Failed to load inventory");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("inventory-table-body").innerHTML = html; // add inventory to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "inventory-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading inventory...</td></tr>`;
  }
};

const loadLocations = async () => {
  console.log("loadLocations called");
  try {
    const response = await fetch("/inventra/locations/");

    if (!response.ok) {
      throw new Error("Failed to load locations");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("locations-table-body").innerHTML = html; // add locations to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "locations-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading locations...</td></tr>`;
  }
};

const loadCompanies = async () => {
  console.log("loadCompanies called");
  try {
    const response = await fetch("/inventra/companies/");

    if (!response.ok) {
      throw new Error("Failed to load company");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("company-table-body").innerHTML = html; // add company to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "company-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading company information...</td></tr>`;
  }
};

const loadUsers = async () => {
  console.log("loadUsers called");
  try {
    const response = await fetch("/inventra/users/");

    if (!response.ok) {
      throw new Error("Failed to load users");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("users-table-body").innerHTML = html; // add users to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "users-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading users...</td></tr>`;
  }
};

const checkUserPermissions = async () => {
  try {
    const response = await fetch(`/inventra/user/permissions`);
    const data = await response.json();

    const userTab = document.getElementById("users-link");
    const auditTab = document.getElementById("audit-link");
    const inventoryTab = document.getElementById("inventory-link");
    const locationTab = document.getElementById("location-link");
    const companyTab = document.getElementById("company-link");
    const addUserBtn = document.getElementById("add-user-btn");
    const editUserBtns = document.getElementsByClassName("edit-user-btn");
    const locationDropdown = document.getElementById("edit-user-location");
    const companyPermGroup = document.getElementById("cmpy-perm-group");
    const locationPermGroup = document.getElementById("loc-perm-group");
    const deleteUserBtns = document.getElementsByClassName("delete-user-btn");
    const editCompanyBtn = document.getElementById("edit-company-btn");
    const addProductBtn = document.getElementById("add-product-btn");
    const editProductBtns = document.getElementsByClassName("edit-product-btn");
    const deleteProductBtns =
      document.getElementsByClassName("delete-product-btn");
    const addInventoryBtns =
      document.getElementsByClassName("add-inventory-btn");
    const editInventoryBtns =
      document.getElementsByClassName("edit-inventory-btn");
    const deleteInventoryBtns = document.getElementsByClassName(
      "delete-inventory-btn"
    );
    const addLocationBtn = document.getElementById("add-location-btn");
    const editLocationBtns =
      document.getElementsByClassName("edit-location-btn");
    const deleteLocationBtns = document.getElementsByClassName(
      "delete-location-btn"
    );
    const locationName = document.getElementById("location-name");
    const address1 = document.getElementById("location-address1");
    const address2 = document.getElementById("location-address2");

    // disable/enable add user button
    if (addUserBtn) {
      addUserBtn.disabled = !(data.canAddUser > 0 || data.canAddRemoveUser > 0);
    }

    // disable/enable delete user buttons
    if (deleteUserBtns.length > 0) {
      for (let i = 0; i < deleteUserBtns.length; i++) {
        deleteUserBtns[i].disabled = !(
          data.canRemoveUser > 0 || data.canAddRemoveUser > 0
        );
      }
    }

    // if user has only location permission manager access
    if (
      data.canManageUserCompanyPerm === 0 &&
      data.canManageUserLocationPerm > 0
    ) {
      // Clear existing options and add only the user’s home location to the dropdown
      locationDropdown.innerHTML = "";
      const option = document.createElement("option");
      option.value = data.locationId;
      option.textContent = data.locationName;
      locationDropdown.appendChild(option);
    } else {
      loadLocationDropdown(true);
    }

    // disable/enable edit user buttons
    if (editUserBtns.length > 0) {
      for (let i = 0; i < editUserBtns.length; i++) {
        editUserBtns[i].disabled = !(
          data.canManageUserCompanyPerm > 0 ||
          data.canManageUserLocationPerm > 0
        );
      }
    }

    // disable/enable add location button
    if (addLocationBtn) {
      addLocationBtn.disabled = !(data.canManageCompaniesAndLocations > 0);
    }

    // disable/enable delete location buttons
    if (deleteLocationBtns.length > 0) {
      for (let i = 0; i < deleteLocationBtns.length; i++) {
        deleteLocationBtns[i].disabled = !(
          data.canManageCompaniesAndLocations > 0
        );
      }
    }

    locationName.disabled = !(data.canChangeLocationName > 0);
    address1.disabled = !(data.canChangeLocationAddress > 0);
    address2.disabled = !(data.canChangeLocationAddress > 0);

    // disable/enable edit location buttons
    if (editLocationBtns.length > 0) {
      for (let i = 0; i < editLocationBtns.length; i++) {
        editLocationBtns[i].disabled = !(
          data.canChangeLocationName > 0 ||
          data.canChangeLocationAddress > 0 ||
          data.canManageCompaniesAndLocations > 0
        );
      }
    }

    // disable/enable company permission checkbox group
    if (companyPermGroup) {
      companyPermGroup.style.display =
        data.canManageUserCompanyPerm > 0 ? "block" : "none";
    }

    // show/hide location permission checkbox group
    if (locationPermGroup) {
      locationPermGroup.style.display =
        data.canManageUserLocationPerm > 0 ? "block" : "none";
    }

    // disable/enable add product button
    if (addProductBtn) {
      addProductBtn.disabled = !(data.canAddRemoveProduct > 0);
    }

    // disable/enable edit product buttons
    if (editProductBtns.length > 0) {
      for (let i = 0; i < editProductBtns.length; i++) {
        editProductBtns[i].disabled = !(data.canEditProduct > 0);
      }
    }

    // disable/enable delete product buttons
    if (deleteProductBtns.length > 0) {
      for (let i = 0; i < deleteProductBtns.length; i++) {
        deleteProductBtns[i].disabled = !(data.canAddRemoveProduct > 0);
      }
    }

    // disable/enable add inventory button
    if (addInventoryBtns.length > 0) {
      for (let i = 0; i < addInventoryBtns.length; i++) {
        addInventoryBtns[i].disabled = !(data.canManageStock > 0);
      }
    }

    // disable/enable edit inventory buttons
    if (editInventoryBtns.length > 0) {
      for (let i = 0; i < editInventoryBtns.length; i++) {
        editInventoryBtns[i].disabled = !(data.canManageStock > 0);
      }
    }

    // disable/enable delete inventory buttons
    if (deleteInventoryBtns.length > 0) {
      for (let i = 0; i < deleteInventoryBtns.length; i++) {
        deleteInventoryBtns[i].disabled = !(data.canManageStock > 0);
      }
    }

    // disable/enable edit company button
    if (editCompanyBtn) {
      editCompanyBtn.disabled = !(data.canManageCompaniesAndLocations > 0);
    }

    // show/hide user tab
    userTab.style.display =
      data.canAddRemoveUser > 0 ||
      data.canRemoveUser > 0 ||
      data.canAddUser > 0 ||
      data.canManageUserCompanyPerm > 0 ||
      data.canManageUserLocationPerm > 0
        ? "block"
        : "none";

    // show/hide audit tab
    auditTab.style.display =
      data.canViewCompanyAudit > 0 || data.canViewLocationAudit > 0
        ? "block"
        : "none";

    inventoryTab.style.display = data.canViewStock > 0 ? "block" : "none";

    locationTab.style.display =
      data.canChangeLocationName > 0 ||
      data.canChangeLocationAddress > 0 ||
      data.canManageCompaniesAndLocations > 0
        ? "block"
        : "none";

    companyTab.style.display =
      data.canManageCompaniesAndLocations > 0 ? "block" : "none";
  } catch (error) {
    console.error("Fetch Error:", error);
  }
};

document.addEventListener("DOMContentLoaded", async () => {
  // on page load, highlight active tab, load and show content, toggle visible tabs/buttons based on user permissions.
  await checkUserPermissions();
  await showSection(
    "products-section",
    document.getElementById("products-link")
  );
});

// // show/hide add user button
// addUserBtn.style.display =
//   data.canAddUser > 0 || data.canAddRemoveUser > 0 ? "block" : "none";

// Show different sections
async function showSection(sectionName, element) {
  const product = { name: "products-section", loadFn: loadProducts };
  const inventory = { name: "inventory-section", loadFn: loadInventory };
  const audit = { name: "audit-section", loadFn: loadAuditTable };
  const users = { name: "users-section", loadFn: loadUsers };
  const locations = { name: "locations-section", loadFn: loadLocations };
  const companies = { name: "companies-section", loadFn: loadCompanies };
  const myAccount = { name: "my-account-section", loadFn: null };

  const sections = [
    product,
    inventory,
    audit,
    users,
    locations,
    companies,
    myAccount,
  ];
  sections.forEach((section) => {
    const sectionElement = document.getElementById(section.name);
    if (sectionElement) {
      sectionElement.style.display = "none";
    }
  });

  // Find active section
  const activeSection = sections.find(
    (section) => section.name === sectionName
  );

  // Load selected section and the table content
  if (activeSection) {
    const activeSectionElement = document.getElementById(activeSection.name);
    if (activeSectionElement) {
      activeSectionElement.style.display = "block";
    }
    if (activeSection.loadFn) {
      await activeSection.loadFn(); // wait for the load function to finish
    }
  }

  await checkUserPermissions();

  // Unhighlight non-active nav item
  document.querySelectorAll(".nav-item").forEach((item) => {
    item.classList.remove("active");
  });
  // Highlight active nav item
  element.classList.add("active");
}

function showNotification(message = "Something went wrong", type = "error") {
  const toast = document.getElementById("toast");
  // add message
  toast.textContent = message;
  toast.classList.remove("error", "success");
  toast.classList.add("show", type);
  // Remove toast after 3 seconds
  setTimeout(() => {
    toast.classList.remove("show");
  }, 3000);
}

function closeModal(modalName) {
  const selectedModal = document.getElementById(modalName + "-modal");
  const selectedForm = document.getElementById(modalName + "-form");
  if (selectedModal) {
    selectedModal.style.display = "none";
    selectedForm.reset();
  }
  if (modalName === "user") {
    document.getElementById("invitation-link").textContent = "";
  }
}

const deleteRowAnimation = (element) => {
  const row = element.closest("tr");
  // Add fade out animation
  row.style.transition = "all 0.3s ease";
  row.style.opacity = "0";
  row.style.transform = "translateX(-20px)";

  // after fade-out, remove from DOM
  setTimeout(() => {
    row.remove();
  }, 300);
};

const confirmDelete = (button, section) => {
  const row = button.closest("tr");
  const rowIndex = section === "user" ? 3 : 1;
  const rowNameToDelete = row.cells[rowIndex].textContent;
  return confirm(
    `Are you sure you want to delete this ${section}: ${rowNameToDelete}?`
  );
};

function showProductModal(product = null) {
  const productModal = document.getElementById("product-modal");
  const form = document.getElementById("product-form");
  if (!productModal || !form) {
    return;
  }
  productModal.style.display = "block";

  // if no product is passed, reset the form (new product)
  if (!product) {
    document.getElementById("product-id").value = "";
    document.getElementById("product-name").value = "";
    document.getElementById("product-price").value = "";
    document.getElementById("product-description").value = "";
    document.getElementById("product-modal-title").textContent = "Add Product";
    document.getElementById("product-modal-btn").textContent = "Add";
  } else {
    // if product is passed, fill the form (existing product)
    document.getElementById("product-id").value = product.productId;
    document.getElementById("product-name").value = product.name;
    document.getElementById("product-price").value = product.price;
    document.getElementById("product-description").value = product.description;
    document.getElementById("product-modal-title").textContent = "Edit Product";
    document.getElementById("product-modal-btn").textContent = "Update";
  }
}

const addProduct = async (formData) => {
  try {
    const response = await fetch("/inventra/products/", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }
    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    closeModal("product");
    showNotification(data.message, "success");
    loadProducts(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateProduct = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/products/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    closeModal("product");
    showNotification(data.message, "success");
    loadProducts(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("product-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const productId = document.getElementById("product-id").value;
    const name = document.getElementById("product-name").value;
    const price = document.getElementById("product-price").value;
    const description = document.getElementById("product-description").value;

    const formData = new URLSearchParams();
    formData.append("name", name);
    formData.append("price", price);
    formData.append("description", description);
    formData.append("action", productId ? "update" : "add");

    if (productId) {
      await updateProduct(productId, formData);
    } else {
      await addProduct(formData);
    }
  });

const deleteProduct = async (productId, element, section) => {
  userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }
  try {
    const response = await fetch(`/inventra/products/${productId}`, {
      method: "DELETE",
    });

    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    showNotification(data.message, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

function showInventoryModal(product, inventory) {
  const inventoryModal = document.getElementById("inventory-modal");
  const form = document.getElementById("inventory-form");
  if (!inventoryModal || !form) {
    return;
  }
  inventoryModal.style.display = "block";

  // if product is passed instead of inventory, prefill everything except quantity
  if (!inventory) {
    document.getElementById("inv-product-id").value = product.productId || "";
    document.getElementById("inv-product-sku").value = product.sku || "";
    document.getElementById("inv-product-name").value = product.name || "";
    document.getElementById("inv-product-price").value = product.price || "";
    document.getElementById("inv-product-qty").value = "";
    document.getElementById("inv-action").value = "add"; // flag for add
    document.getElementById("inventory-modal-title").textContent =
      "Add to Product Inventory";
    document.getElementById("inv-modal-btn").textContent = "Add";
  } else {
    // if inventory is passed instead of product, pre-fill everything
    document.getElementById("inv-product-id").value = inventory.productId || "";
    document.getElementById("inv-product-sku").value = inventory.sku || "";
    document.getElementById("inv-product-name").value = inventory.name || "";
    document.getElementById("inv-product-price").value = inventory.price || "";
    document.getElementById("inv-product-qty").value = inventory.quantity;
    document.getElementById("inv-action").value = "update"; // flag for update
    document.getElementById("inventory-modal-title").textContent =
      "Update Product in Inventory";
    document.getElementById("inv-modal-btn").textContent = "Update";
  }
}

const addInventory = async (formData) => {
  try {
    const response = await fetch("/inventra/inventory", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });
    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }
    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    closeModal("inventory");
    showNotification(data.message, "success");
    loadInventory(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateInventory = async (formData) => {
  try {
    const response = await fetch(`/inventra/inventory/`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    const data = await response.json();
    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    // if success
    closeModal("inventory");
    showNotification(data.message, "success");
    loadInventory(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("inventory-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const productId = document.getElementById("inv-product-id").value;
    const quantity = document.getElementById("inv-product-qty").value;
    const action = document.getElementById("inv-action").value;

    const formData = new URLSearchParams();
    formData.append("productId", productId);
    formData.append("quantity", quantity);
    formData.append("action", action);

    if (action === "update") {
      await updateInventory(formData);
    } else {
      await addInventory(formData);
    }
  });

const deleteInventory = async (productId, locationId, element, section) => {
  userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }

  try {
    const response = await fetch(
      `/inventra/inventory/${locationId}/${productId}`,
      {
        method: "DELETE",
      }
    );

    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    showNotification(data.message, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

function showLocationModal(location = null) {
  const locationModal = document.getElementById("location-modal");
  const form = document.getElementById("location-form");
  if (!locationModal || !form) {
    return;
  }
  locationModal.style.display = "block";

  // if no location is passed, reset the form (new location)
  if (!location) {
    document.getElementById("location-id").value = "";
    document.getElementById("location-name").value = "";
    document.getElementById("location-address1").value = "";
    document.getElementById("location-address2").value = "";
    document.getElementById("location-modal-title").textContent =
      "Add Location";
    document.getElementById("location-modal-btn").textContent = "Add";
  } else {
    // if location is passed, fill the form (existing location)
    document.getElementById("location-id").value = location.locationId;
    document.getElementById("location-name").value = location.name;
    document.getElementById("location-address1").value = location.address1;
    document.getElementById("location-address2").value = location.address2;
    document.getElementById("location-modal-title").textContent =
      "Edit Location";
    document.getElementById("location-modal-btn").textContent = "Update";
  }
}

const addLocation = async (formData) => {
  try {
    const response = await fetch("/inventra/locations", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    const data = await response.json();
    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    // if success
    closeModal("location");
    showNotification(data.message, "success");
    loadLocations(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateLocation = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/locations/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }
    const data = await response.json();
    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    closeModal("location");
    showNotification(data.message, "success");
    loadLocations(); // reload the table
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("location-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const locationId = document.getElementById("location-id").value;
    const name = document.getElementById("location-name").value;
    const address1 = document.getElementById("location-address1").value;
    const address2 = document.getElementById("location-address2").value;

    const formData = new URLSearchParams();
    formData.append("name", name);
    formData.append("address_1", address1);
    formData.append("address_2", address2);
    formData.append("action", locationId ? "update" : "add");

    if (locationId) {
      await updateLocation(locationId, formData);
    } else {
      await addLocation(formData);
    }
  });

const deleteLocation = async (locationId, element, section) => {
  userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }
  try {
    const response = await fetch(`/inventra/locations/${locationId}`, {
      method: "DELETE",
    });

    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    showNotification(data.message, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

function showCompanyModal(company = null) {
  const companyModal = document.getElementById("company-modal");
  const form = document.getElementById("company-form");
  if (!companyModal || !form) {
    return;
  }
  companyModal.style.display = "block";

  // if no company is passed, reset the form (new company)
  if (!company) {
    document.getElementById("company-id").value = "";
    document.getElementById("company-name").value = "";
    document.getElementById("company-modal-title").textContent = "Add Company";
    document.getElementById("company-modal-btn").textContent = "Add";
  } else {
    // if company is passed, fill the form (existing company)
    document.getElementById("company-id").value = company.companyId;
    document.getElementById("company-name").value = company.name;
    document.getElementById("company-modal-title").textContent = "Edit Company";
    document.getElementById("company-modal-btn").textContent = "Update";
  }
}

const addCompany = async (formData) => {
  try {
    const response = await fetch("/inventra/companies/", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    const data = await response.json();
    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    // if success
    closeModal("company");
    showNotification(data.message, "success");
    loadCompanies();
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateCompany = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/companies/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });
    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    const data = await response.json();

    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    closeModal("company");
    showNotification("Company updated successfully!", "success");
    loadCompanies();
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("company-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const companyId = document.getElementById("company-id").value;
    const name = document.getElementById("company-name").value;

    const formData = new URLSearchParams();
    formData.append("name", name);
    formData.append("action", companyId ? "update" : "add");

    if (companyId) {
      await updateCompany(companyId, formData);
    } else {
      await addCompany(formData);
    }
  });

const deleteCompany = async (companyId, element, section) => {
  userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }
  try {
    const response = await fetch(`/inventra/companies/${companyId}`, {
      method: "DELETE",
    });

    const msg = await response.json();

    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    showNotification(data.message, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

const loadLocationDropdown = async (isEdit = false) => {
  const dropdown = isEdit
    ? document.getElementById("edit-user-location")
    : document.getElementById("add-user-location");
  try {
    const response = await fetch("/inventra/location-dropdown");

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const locations = await response.json();

    // Prevent duplicate options if modal is opened multiple times
    dropdown.innerHTML = "";

    if (Array.isArray(locations)) {
      // User has company-wide access (received a list)
      locations.forEach((location) => {
        const option = document.createElement("option");
        option.value = location.locationId;
        option.textContent = location.name;
        dropdown.appendChild(option);
      });
    } else if (locations && locations.locationId) {
      // User has location-specific access (received a single location object)
      const option = document.createElement("option");
      option.value = locations.locationId;
      option.textContent = locations.name;
      dropdown.appendChild(option);
    }

    // async returns a promise
    return dropdown;
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

const showUserModal = async () => {
  await loadLocationDropdown();
  const userModal = document.getElementById("user-modal");
  const form = document.getElementById("user-form");

  if (!userModal || !form) {
    return;
  }

  userModal.style.display = "block";
};

async function showEditUserModal(user) {
  // waits for the dropdown to be populated before modyfying
  const dropdown = await loadLocationDropdown(true);
  const userModal = document.getElementById("edit-user-modal");
  const form = document.getElementById("edit-user-form");

  if (!userModal || !form) {
    return;
  }
  userModal.style.display = "block";

  if (user) {
    document.getElementById("edit-user-lastname").value = user.lastName;
    document.getElementById("edit-user-firstname").value = user.firstName;
    document.getElementById("edit-user-id").value = user.userId;
    document.getElementById("edit-user-email").value = user.email;
    dropdown.value = user.locationId;
    document.getElementById("cmpy-view-audit").checked = user.compViewAudit > 0; // 1 = true, 0 = false
    document.getElementById("cmpy-add-rmv-user").checked =
      user.compAddRemoveUser > 0;
    document.getElementById("cmpy-perm-manager").checked =
      user.compManageUserCompanyPerm > 0;
    document.getElementById("cmpy-edit-name").checked = user.compChangeName > 0;
    document.getElementById("cmpy-add-rmv-products").checked =
      user.compAddRemoveProduct > 0;
    document.getElementById("cmpy-edit-products").checked =
      user.compEditProduct > 0;
    document.getElementById("loc-view-audit").checked = user.locViewAudit > 0;
    document.getElementById("loc-add-user").checked = user.locAddUser > 0;
    document.getElementById("loc-rmv-user").checked = user.locRemoveUser > 0;
    document.getElementById("loc-perm-manager").checked =
      user.locManageUserLocationPerm > 0;
    document.getElementById("loc-edit-name").checked = user.locChangeName > 0;
    document.getElementById("loc-edit-addresses").checked =
      user.locChangeAddress > 0;
    document.getElementById("loc-view-stock").checked = user.locViewStock > 0;
    document.getElementById("loc-edit-stock").checked = user.locManageStock > 0;
  }
}

const deleteUser = async (userId, element, section) => {
  const userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }
  try {
    const response = await fetch(`/inventra/users/${userId}`, {
      method: "DELETE",
    });

    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }

    showNotification(data.message, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

const addUser = async (formData) => {
  try {
    const response = await fetch("/inventra/users/", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    // if not logged in, redirect
    if (response.status == 401) {
      window.location.href = "/inventra/index";
      return;
    }

    const data = await response.json();

    const inviteLink = document.getElementById("invitation-link");
    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      inviteLink.textContent = "";
      return;
    }
    // if success - show invite link
    inviteLink.textContent = data.link;
    showNotification(data.message, "success");
    await loadUsers();
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateUser = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/users/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    const data = await response.json();

    // if error
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    // if success
    closeModal("edit-user");
    showNotification(data.message, "success");
    await loadUsers();
    await checkUserPermissions(); //recheck permissions
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document.getElementById("user-form").addEventListener("submit", async (e) => {
  e.preventDefault(); // prevent full page reload

  const email = document.getElementById("user-email").value;
  const locationId = document.getElementById("add-user-location").value;

  const formData = new URLSearchParams();
  formData.append("email", email);
  formData.append("locationId", locationId);
  formData.append("action", "add");

  await addUser(formData);
});

document
  .getElementById("edit-user-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const userId = document.getElementById("edit-user-id").value;
    const lastName = document.getElementById("edit-user-lastname").value;
    const firstName = document.getElementById("edit-user-firstname").value;
    const email = document.getElementById("edit-user-email").value;
    const locationId = document.getElementById("edit-user-location").value;
    const hierarchy = document.getElementById("edit-user-hierarchy").value;
    const compViewAudit = document.getElementById("cmpy-view-audit").checked
      ? 1
      : 0;
    const compAddRemoveUser = document.getElementById("cmpy-add-rmv-user")
      .checked
      ? 1
      : 0;
    const compManageUserCompanyPerm = document.getElementById(
      "cmpy-perm-manager"
    ).checked
      ? 1
      : 0;
    const compChangeName = document.getElementById("cmpy-edit-name").checked
      ? 1
      : 0;
    const compAddRemoveProduct = document.getElementById(
      "cmpy-add-rmv-products"
    ).checked
      ? 1
      : 0;
    const compEditProduct = document.getElementById("cmpy-edit-products")
      .checked
      ? 1
      : 0;
    const locViewAudit = document.getElementById("loc-view-audit").checked
      ? 1
      : 0;
    const locAddUser = document.getElementById("loc-add-user").checked ? 1 : 0;
    const locRemoveUser = document.getElementById("loc-rmv-user").checked
      ? 1
      : 0;
    const locManageUserLocationPerm = document.getElementById(
      "loc-perm-manager"
    ).checked
      ? 1
      : 0;
    const locChangeName = document.getElementById("loc-edit-name").checked
      ? 1
      : 0;
    const locChangeAddress = document.getElementById("loc-edit-addresses")
      .checked
      ? 1
      : 0;
    const locViewStock = document.getElementById("loc-view-stock").checked
      ? 1
      : 0;
    const locManageStock = document.getElementById("loc-edit-stock").checked
      ? 1
      : 0;

    const formData = new URLSearchParams();
    formData.append("lastName", lastName);
    formData.append("firstName", firstName);
    formData.append("email", email);
    formData.append("locationId", locationId);
    formData.append("hierarchy", hierarchy);
    formData.append("compViewAudit", compViewAudit);
    formData.append("compAddRemoveUser", compAddRemoveUser);
    formData.append("compManageUserCompanyPerm", compManageUserCompanyPerm);
    formData.append("compChangeName", compChangeName);
    formData.append("compAddRemoveProduct", compAddRemoveProduct);
    formData.append("compEditProduct", compEditProduct);
    formData.append("locViewAudit", locViewAudit);
    formData.append("locAddUser", locAddUser);
    formData.append("locRemoveUser", locRemoveUser);
    formData.append("locManageUserLocationPerm", locManageUserLocationPerm);
    formData.append("locChangeName", locChangeName);
    formData.append("locChangeAddress", locChangeAddress);
    formData.append("locViewStock", locViewStock);
    formData.append("locManageStock", locManageStock);
    formData.append("action", "update");

    await updateUser(userId, formData);
  });

const updateUserProfile = async (formData) => {
  try {
    const response = await fetch(`/inventra/user/`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });
    if (response.status === 401) {
      window.location.href = "/inventra/index";
      return;
    }
    const data = await response.json();
    if (!data.success) {
      showNotification(data.message, "error");
      return;
    }
    showNotification(data.message, "success");
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("my-account-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const lastName = document.getElementById("myuser-lastname").value;
    const firstName = document.getElementById("myuser-firstname").value;

    const formData = new URLSearchParams();
    formData.append("lastName", lastName);
    formData.append("firstName", firstName);

    await updateUserProfile(formData);
  });
