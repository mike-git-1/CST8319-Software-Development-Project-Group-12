<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty company}">
  <tr>
    <td colspan="5">Company not found.</td>
  </tr>
</c:if>

<tr>
  <td><c:out value="${company.name}" /></td>
  <td><c:out value="${company.dateCreated}" /></td>
  <td>
    <button
      class="action-btn edit"
      id="edit-company-btn"
      onclick='showCompanyModal({
          companyId: "${company.companyId}",
          name: "${company.name}",
          dateCreated: "${company.dateCreated}",
        })'
    >
      Edit
    </button>
    <button
      class="action-btn delete"
      style="display: none"
      onclick="deleteCompany(`${company.companyId}`,this,'company')"
    >
      Delete
    </button>
  </td>
</tr>
