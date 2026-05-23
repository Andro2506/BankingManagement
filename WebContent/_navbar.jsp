<%-- Shared navigation bar (included by other JSPs). --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Employee" %>
<%
    Employee navEmp = (Employee) session.getAttribute("loggedInEmployee");
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-3">
  <div class="container-fluid">
    <a class="navbar-brand" href="<%= request.getContextPath() %>/home.jsp">Banking System</a>
    <div class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/customers">Customers</a></li>
        <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/transactions">Transactions</a></li>
        <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/loans">Loans</a></li>
      </ul>
      <ul class="navbar-nav">
        <% if (navEmp != null) { %>
          <li class="nav-item"><span class="nav-link disabled text-light">Hi, <%= navEmp.getFirstName() %></span></li>
          <li class="nav-item"><a class="nav-link" href="<%= request.getContextPath() %>/logout">Logout</a></li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>
