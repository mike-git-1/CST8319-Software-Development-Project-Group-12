<%@ page contentType="text/html;charset=UTF-8" %> <%@ taglib
uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:if test="${empty audit_logs}">
  <tr>
    <td colspan="5">No logs found.</td>
  </tr>
</c:if>

<c:forEach var="audit" items="${audit_logs}">
  <tr>
    <td><c:out value="${audit.auditId}" /></td>
    <td><c:out value="${audit.datetime}" /></td>
    <td>
      <c:choose>
        <c:when test="${not empty audit.location}">
          <c:out value="${audit.location}" />
        </c:when>
        <c:otherwise> Global </c:otherwise>
      </c:choose>
    </td>
    <td><c:out value="${audit.user}" /></td>
    <td>
      <span
        class="audit-action-badge ${audit.changeType == 'ADDED' ? 'badge-add' : audit.changeType == 'MODIFIED' ? 'badge-modify' : audit.changeType == 'REMOVED' ? 'badge-remove' : ''}"
        ><c:out value="${audit.changeType}" />
      </span>
    </td>
    <td><c:out value="${audit.changeTargetId}" /></td>
    <td>
      <span
        class="audit-target-badge ${ audit.changeTarget == 'USER' ? 'badge-user' : audit.changeTarget == 'COMPANY' ? 'badge-company' : audit.changeTarget == 'PERMISSION' ? 'badge-permission' : audit.changeTarget == 'PRODUCT' ? 'badge-product' : audit.changeTarget == 'LOCATION' ? 'badge-location' : audit.changeTarget == 'INVENTORY' ? 'badge-inventory' : ''}"
        ><c:out value="${audit.changeTarget}" />
      </span>
    </td>
    <td><c:out value="${audit.changeColumn}" /></td>
    <td>
      <c:choose>
        <c:when test="${audit.previousValue != '-'}">
          <span style="color: red; margin-right: 5px">▼</span>
          "<c:out value="${audit.previousValue}" />"
        </c:when>
        <c:otherwise>
          <c:out value="${audit.previousValue}" />
        </c:otherwise>
      </c:choose>
    </td>
    <td>
      <c:choose>
        <c:when test="${audit.newValue != '-'}">
          <span style="color: green; margin-right: 5px">▲</span>
          "<c:out value="${audit.newValue}" />"
        </c:when>
        <c:otherwise>
          <c:out value="${audit.newValue}" />
        </c:otherwise>
      </c:choose>
    </td>
  </tr>
</c:forEach>
