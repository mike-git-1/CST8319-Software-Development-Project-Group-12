// Mobile menu toggle
const hamburger = document.getElementById("hamburger");
const navMenu = document.getElementById("navMenu");

hamburger.addEventListener("click", () => {
  hamburger.classList.toggle("active");
  navMenu.classList.toggle("active");
});

// Close mobile menu when clicking on a link
document.querySelectorAll(".nav-link").forEach((link) => {
  link.addEventListener("click", () => {
    hamburger.classList.remove("active");
    navMenu.classList.remove("active");
  });
});

// Popup form
const showPopupBtn = document.querySelector(".login-btn");
const hidePopupBtn = document.querySelector(".form-popup .close-btn");
const inputs = document.querySelectorAll(".form-popup input");

// show popup form
showPopupBtn.addEventListener("click", () => {
  document.body.classList.toggle("show-popup");
});

// hide popup form
hidePopupBtn.addEventListener("click", () => {
  showPopupBtn.click();
  inputs.forEach((input) => {
    input.value = "";
  });
});

document.addEventListener("DOMContentLoaded", () => {
  const params = new URLSearchParams(window.location.search);
  const page = params.get("page");

  if (page === "login") {
    showPopupBtn.click();
  }
});
