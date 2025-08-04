<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty companies}">
  <tr>
    <td colspan="5">No companies found.</td>
  </tr>
</c:if>

<c:forEach var="company" items="${companies}">
  <tr>
    <td><c:out value="${company.name}" /></td>
    <td><c:out value="${company.dateCreated}" /></td>
    <td>
      <button
        class="action-btn edit"
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
        onclick="deleteCompany(`${company.companyId}`,this,'company')"
      >
        Delete
      </button>
    </td>
  </tr>
</c:forEach>
