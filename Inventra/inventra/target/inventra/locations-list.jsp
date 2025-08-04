<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty locations}">
  <tr>
    <td colspan="5">No locations found.</td>
  </tr>
</c:if>

<c:forEach var="location" items="${locations}">
  <tr>
    <td><c:out value="${location.name}" /></td>
    <td><c:out value="${location.address1}" /></td>
    <td><c:out value="${location.address2}" /></td>
    <td>
      <button
        class="action-btn edit"
        onclick='showLocationModal({
          locationId: "${location.locationId}",
          name: "${location.name}",
          address1: "${location.address1}",
          address2: "${location.address2}",
        })'
      >
        Edit
      </button>
      <button
        class="action-btn delete"
        onclick="deleteLocation(`${location.locationId}`,this,'location')"
      >
        Delete
      </button>
    </td>
  </tr>
</c:forEach>
