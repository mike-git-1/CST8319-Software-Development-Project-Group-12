<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${empty users}">
  <tr>
    <td colspan="5">No users found.</td>
  </tr>
</c:if>

<c:forEach var="user" items="${users}">
  <tr>
    <td>
      <div class="avatar-table">
        ${user.first_name.charAt(0)}${user.last_name.charAt(0)}
      </div>
    </td>
    <td>${user.last_name}</td>
    <td>${user.first_name}</td>
    <td>${user.email}</td>
    <td>WIP</td>
    <td>
      <span class="status-badge ${user.verified ? 'active' : 'inactive'}"
        >${user.verified ? 'VERIFIED' : 'PENDING'}
      </span>
    </td>
    <td>
      <fmt:formatDate value="${user.date_created}" pattern="yyyy-MM-dd" />
    </td>
    <td>
      <button
        class="action-btn edit"
        onclick='showUserModal({
          userId: "${user.id}",
          firstName: "${user.first_name}",
          lastName: "${user.last_name}",
          email: "${user.email}"
        })'
      >
        Edit
      </button>
      <button
        class="action-btn delete"
        onclick="deleteUser(`${user.id}`,this,'user')"
      >
        Delete
      </button>
    </td>
  </tr>
</c:forEach>
