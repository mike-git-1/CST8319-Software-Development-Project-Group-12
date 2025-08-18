<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty inventories}">
  <tr>
    <td colspan="5">No products found in inventory.</td>
  </tr>
</c:if>
<c:forEach var="inventory" items="${inventories}">
  <tr>
    <td><c:out value="${inventory.sku}" /></td>
    <td><c:out value="${inventory.name}" /></td>
    <td><c:out value="${inventory.location}" /></td>
    <td>
      <fmt:formatNumber
        value="${inventory.price}"
        type="currency"
        currencySymbol="$"
        minFractionDigits="2"
        maxFractionDigits="2"
      />
    </td>
    <td><c:out value="${inventory.quantity}" /></td>
    <td>
      <span class="status-badge ${inventory.quantity > 0 ? 'active' : 'danger'}"
        >${inventory.quantity > 0 ? 'IN STOCK' : '&#9888; OUT OF STOCK'}</span
      >
    </td>
    <td>
      <button
        class="action-btn edit edit-inventory-btn"
        onclick="showInventoryModal(null,{
          productId: '${inventory.productId}',
          sku: '${inventory.sku}',
          name: '${inventory.name}',
          price: '${inventory.price}',
          quantity: '${inventory.quantity}'
        })"
      >
        Edit
      </button>
      <button
        class="action-btn delete delete-inventory-btn"
        onclick="deleteInventory(`${inventory.productId}`,`${inventory.locationId}`,this,'inventory')"
      >
        Delete
      </button>
    </td>
  </tr>
</c:forEach>
