<%-- US005: Confirm customer deletion --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Customer c = (Customer) request.getAttribute("customer");
    if (c == null) {
        response.sendRedirect(request.getContextPath() + "/customers");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delete Customer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <div class="card border-danger">
        <div class="card-header bg-danger text-white">Confirm Delete</div>
        <div class="card-body">
            <p>You are about to <strong>delete</strong> the following customer:</p>
            <table class="table">
                <tr><th>SSN</th><td><%= c.getCustomerSsnId() %></td></tr>
                <tr><th>Name</th><td><%= c.getFullName() %></td></tr>
                <tr><th>Email</th><td><%= c.getEmail() %></td></tr>
                <tr><th>Account</th><td><%= c.getAccountNumber() %></td></tr>
                <tr><th>Balance</th><td><%= c.getAccountBalance() %></td></tr>
            </table>
            <form action="<%= request.getContextPath() %>/customer/delete" method="post">
                <input type="hidden" name="customerSsnId" value="<%= c.getCustomerSsnId() %>">
                <button type="submit" class="btn btn-danger">Yes, Delete</button>
                <a href="<%= request.getContextPath() %>/customers" class="btn btn-secondary">Cancel</a>
            </form>
        </div>
    </div>
</div>
</body>
</html>
