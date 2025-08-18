<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty products}">
  <tr>
    <td colspan="5">No products found.</td>
  </tr>
</c:if>

<c:forEach var="product" items="${products}">
  <tr>
    <td><c:out value="${product.sku}" /></td>
    <td><c:out value="${product.name}" /></td>
    <td><c:out value="${product.description}" /></td>
    <td>
      <fmt:formatNumber
        value="${product.price}"
        type="currency"
        currencySymbol="$"
        minFractionDigits="2"
        maxFractionDigits="2"
      />
    </td>
    <td>
      <button
        class="action-btn edit edit-product-btn"
        onclick='showProductModal({
          productId: "${product.productId}",
          name: "${product.name}",
          price: "${product.price}",
          description: "${product.description}"
        })'
      >
        Edit
      </button>
      <button
        class="action-btn delete delete-product-btn"
        onclick="deleteProduct(`${product.productId}`,this,'product')"
      >
        Delete
      </button>
      <button
        class="action-btn add-inventory-btn"
        onclick='showInventoryModal({
          productId: "${product.productId}",
          sku: "${product.sku}",
          name: "${product.name}",
          price: "${product.price}"
        },null)'
      >
        <span style="margin-right: 8px">+</span>Add to inventory
      </button>
    </td>
  </tr>
</c:forEach>
