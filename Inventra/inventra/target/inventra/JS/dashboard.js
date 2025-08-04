const loadMyProducts = async () => {
  console.log("loadMyProducts called");
  try {
    const response = await fetch("/inventra/products-list");

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

const loadInventory = async () => {
  console.log("loadInventory called");
  try {
    const response = await fetch("/inventra/inventory-list");

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
    const response = await fetch("/inventra/locations-list");

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
    const response = await fetch("/inventra/companies-list");

    if (!response.ok) {
      throw new Error("Failed to load companies");
    }

    const html = await response.text(); // get jsp output as html string
    document.getElementById("company-table-body").innerHTML = html; // add companies to table
  } catch (error) {
    console.error("Fetch Error:", error);
    document.getElementById(
      "company-table-body"
    ).innerHTML = `<tr><td colspan="5">Error loading companies...</td></tr>`;
  }
};

const loadUsers = async () => {
  console.log("loadUsers called");
  try {
    const response = await fetch("/inventra/users-list");

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

document.addEventListener("DOMContentLoaded", loadMyProducts); // load TABLES on page load

// Show different sections
function showSection(sectionName, element) {
  const product = { name: "products-section", loadFn: loadMyProducts };
  const inventory = { name: "inventory-section", loadFn: loadInventory };
  const analytics = { name: "analytics-section", loadFn: null };
  const users = { name: "users-section", loadFn: loadUsers };
  const locations = { name: "locations-section", loadFn: loadLocations };
  const companies = { name: "companies-section", loadFn: loadCompanies };
  const myAccount = { name: "my-account-section", loadFn: null };

  const sections = [
    product,
    inventory,
    analytics,
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
    if (activeSection.loadFn) activeSection.loadFn();
  }

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
    const response = await fetch("/inventra/products", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });
    const data = await response.json();
    if (data.success) {
      closeModal("product");
      showNotification(data.message, "success");
      loadMyProducts(); // reload the table
    } else {
      showNotification(data.message, "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateProduct = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/product/update/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    if (response.ok) {
      closeModal("product");
      showNotification("Product updated successfully!", "success");
      loadMyProducts(); // reload the table
    } else {
      showNotification("Failed to update product", "error");
    }
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
    // const companyID = 123;

    const formData = new URLSearchParams();
    formData.append("name", name);
    formData.append("price", price);
    formData.append("description", description);
    // formData.append("company_id", companyID);

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
    const response = await fetch(`/inventra/product/delete/${productId}`, {
      method: "POST",
    });

    const msg = await response.text();

    if (!response.ok) {
      throw new Error(msg);
    }

    showNotification(msg, "success");
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
    document.getElementById("inv-product-location-id").value = "";
    document.getElementById("inv-product-sku").value = product.sku || "";
    document.getElementById("inv-product-name").value = product.name || "";
    document.getElementById("inv-product-price").value = product.price || "";
    document.getElementById("inv-product-qty").value = "";
    document.getElementById("inv-mode").value = "add"; // flag for add
    document.getElementById("inventory-modal-title").textContent =
      "Add to Product Inventory";
    document.getElementById("inv-modal-btn").textContent = "Add";
  } else {
    // if inventory is passed instead of product, pre-fill everything
    document.getElementById("inv-product-id").value = inventory.productId || "";
    document.getElementById("inv-product-location-id").value =
      inventory.locationId || "";
    document.getElementById("inv-product-sku").value = inventory.sku || "";
    document.getElementById("inv-product-name").value = inventory.name || "";
    document.getElementById("inv-product-price").value = inventory.price || "";
    document.getElementById("inv-product-qty").value = inventory.quantity;
    document.getElementById("inv-mode").value = "update"; // flag for update
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
    const data = await response.json();
    if (data.success) {
      closeModal("inventory");
      showNotification(data.message, "success");
      loadInventory(); // reload the table
    } else {
      showNotification(data.message, "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateInventory = async (formData) => {
  try {
    const response = await fetch(`/inventra/inventory/update/`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    const data = await response.json();
    if (data.success) {
      closeModal("inventory");
      showNotification(data.message, "success");
      loadInventory(); // reload the table
    } else {
      showNotification(data.message, "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document
  .getElementById("inventory-form")
  .addEventListener("submit", async (e) => {
    e.preventDefault(); // prevent full page reload

    const locationId = document.getElementById("inv-product-location-id").value;
    const productId = document.getElementById("inv-product-id").value;
    const quantity = document.getElementById("inv-product-qty").value;
    const mode = document.getElementById("inv-mode").value;

    const formData = new URLSearchParams();
    formData.append("productId", productId);
    formData.append("locationId", locationId);
    formData.append("quantity", quantity);

    if (mode === "update") {
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
  const formData = new URLSearchParams();
  formData.append("productId", productId);
  formData.append("locationId", locationId);
  try {
    const response = await fetch(`/inventra/inventory/delete/`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    const data = await response.json();
    if (data.success) {
      showNotification(data.message, "success");
      deleteRowAnimation(element);
    } else {
      showNotification(data.message, "error");
    }
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

    if (response.ok) {
      closeModal("location");
      showNotification("Location added successfully!", "success");
      loadLocations(); // reload the table
    } else {
      showNotification("Failed to add LOCATION", "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateLocation = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/location/update/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    if (response.ok) {
      closeModal("location");
      showNotification("Location updated successfully!", "success");
      loadLocations(); // reload the table
    } else {
      showNotification("Failed to update Location", "error");
    }
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
    const response = await fetch(`/inventra/location/delete/${locationId}`, {
      method: "POST",
    });

    const msg = await response.text();

    if (!response.ok) {
      throw new Error(msg);
    }

    showNotification(msg, "success");
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
    const response = await fetch("/inventra/companies", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    if (response.ok) {
      closeModal("company");
      showNotification("Company added successfully!", "success");
      loadCompanies();
    } else {
      showNotification("Failed to add COMPANY", "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

const updateCompany = async (id, formData) => {
  try {
    const response = await fetch(`/inventra/company/update/${id}`, {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    if (response.ok) {
      closeModal("company");
      showNotification("Company updated successfully!", "success");
      loadCompanies();
    } else {
      showNotification("Failed to update company", "error");
    }
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
    const response = await fetch(`/inventra/company/delete/${companyId}`, {
      method: "POST",
    });

    const msg = await response.text();

    if (!response.ok) {
      throw new Error(msg);
    }

    showNotification(msg, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};

function showUserModal(user = null) {
  const userModal = document.getElementById("user-modal");
  const form = document.getElementById("user-form");
  if (!userModal || !form) {
    return;
  }
  userModal.style.display = "block";
}

const deleteUser = async (userId, element, section) => {
  userConfirmed = confirmDelete(element, section);
  if (!userConfirmed) {
    return;
  }
  try {
    const response = await fetch(`/inventra/user/delete/${userId}`, {
      method: "POST",
    });

    const msg = await response.text();

    if (!response.ok) {
      throw new Error(msg);
    }

    showNotification(msg, "success");
    deleteRowAnimation(element);
  } catch (error) {
    console.error("Error:", error.message);
    showNotification("Network error", "error");
  }
};
// const locations = ["New York", "London", "Tokyo"];
// const select = document.getElementById("user-location");

// locations.forEach(loc => {
//   const option = document.createElement("option");
//   option.value = loc;
//   option.textContent = loc;
//   select.appendChild(option);
// });

const addUser = async (formData) => {
  try {
    const response = await fetch("/inventra/users", {
      method: "POST",
      headers: { "Content-Type": "application/x-www-form-urlencoded" },
      body: formData.toString(),
    });

    const data = await response.json();

    const inviteLink = document.getElementById("invitation-link");
    if (data.success) {
      inviteLink.textContent = data.link;
      showNotification(data.message, "success");
    } else {
      showNotification(data.message, "error");
    }
  } catch (error) {
    console.error("Fetch Error:", error);
    showNotification("Network error", "error");
  }
};

document.getElementById("user-form").addEventListener("submit", async (e) => {
  e.preventDefault(); // prevent full page reload

  const email = document.getElementById("user-email").value;

  const formData = new URLSearchParams();
  formData.append("email", email);

  await addUser(formData);
});
