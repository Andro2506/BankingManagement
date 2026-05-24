<%-- Shared navigation bar for customer-portal pages. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer" %>
<%
    // Pull the customer out of the session so we can show their name in the bar
    Customer navCust = (Customer) session.getAttribute("loggedInCustomer");
%>
<nav class="navbar navbar-expand-lg navbar-dark bg-success mb-3">
  <div class="container-fluid">
    <a class="navbar-brand" href="<%= request.getContextPath() %>/customer_home.jsp">My Bank Portal</a>
    <div class="collapse navbar-collapse">
      <ul class="navbar-nav me-auto">
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/customer_home.jsp">Dashboard</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/customer/transaction">Make Transaction</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/customer/loan">Apply for Loan</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/customer/my-transactions">My Transactions</a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="<%= request.getContextPath() %>/customer/my-loans">My Loans</a>
        </li>
      </ul>
      <ul class="navbar-nav">
        <% if (navCust != null) { %>
          <li class="nav-item">
            <span class="nav-link disabled text-light">Hi, <%= navCust.getFirstName() %></span>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="<%= request.getContextPath() %>/customer/logout">Logout</a>
          </li>
        <% } %>
      </ul>
    </div>
  </div>
</nav>
