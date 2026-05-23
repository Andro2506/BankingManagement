<%-- Lists all customers with edit/delete actions --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Customer, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Customer> customers = (List<Customer>) request.getAttribute("customers");
    String flashMessage = (String) session.getAttribute("flashMessage");
    String flashError   = (String) session.getAttribute("flashError");
    if (flashMessage != null) session.removeAttribute("flashMessage");
    if (flashError   != null) session.removeAttribute("flashError");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customers</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>Customers</h3>
        <a href="<%= request.getContextPath() %>/customer/register" class="btn btn-primary">+ Register New</a>
    </div>

    <% if (flashMessage != null) { %><div class="alert alert-success"><%= flashMessage %></div><% } %>
    <% if (flashError   != null) { %><div class="alert alert-danger"><%= flashError %></div><% } %>

    <div class="table-responsive">
        <table class="table table-striped table-hover">
            <thead class="table-dark">
                <tr>
                    <th>SSN</th><th>Name</th><th>Email</th><th>Account</th>
                    <th>Type</th><th>Balance</th><th>Contact</th><th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    if (customers != null && !customers.isEmpty()) {
                        for (int i = 0; i < customers.size(); i++) {
                            Customer c = customers.get(i);
                %>
                    <tr>
                        <td><%= c.getCustomerSsnId() %></td>
                        <td><%= c.getFullName() %></td>
                        <td><%= c.getEmail() %></td>
                        <td><%= c.getAccountNumber() %></td>
                        <td><%= c.getAccountType() %></td>
                        <td><%= c.getAccountBalance() %></td>
                        <td><%= c.getContactNumber() %></td>
                        <td>
                            <a class="btn btn-sm btn-warning"
                               href="<%= request.getContextPath() %>/customer/edit?ssn=<%= c.getCustomerSsnId() %>">Edit</a>
                            <a class="btn btn-sm btn-danger"
                               href="<%= request.getContextPath() %>/customer/delete?ssn=<%= c.getCustomerSsnId() %>">Delete</a>
                        </td>
                    </tr>
                <%      }
                    } else { %>
                    <tr><td colspan="8" class="text-center">No customers yet.</td></tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
