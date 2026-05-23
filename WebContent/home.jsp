<%-- Employee home page (after login) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Boolean justLoggedIn = (Boolean) session.getAttribute("loginSuccess");
    session.removeAttribute("loginSuccess");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Home - Banking Management</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">

<%@ include file="/_navbar.jsp" %>

<div class="container py-4">
    <div class="alert alert-success">
        Welcome, <strong><%= emp.getFullName() %></strong>
        (Employee ID <%= emp.getEmployeeId() %>, <%= emp.getDesignation() %>)
    </div>

    <div class="row g-3">
        <div class="col-md-4">
            <div class="card h-100">
                <div class="card-body">
                    <h5>Customer Management</h5>
                    <p class="small text-muted">Add, edit and delete customer records.</p>
                    <a href="<%= request.getContextPath() %>/customers" class="btn btn-primary btn-sm">View Customers</a>
                    <a href="<%= request.getContextPath() %>/customer/register" class="btn btn-outline-primary btn-sm">Add New</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100">
                <div class="card-body">
                    <h5>Transactions</h5>
                    <p class="small text-muted">Capture credits / debits for customers.</p>
                    <a href="<%= request.getContextPath() %>/transactions" class="btn btn-primary btn-sm">View Transactions</a>
                    <a href="<%= request.getContextPath() %>/transaction" class="btn btn-outline-primary btn-sm">New Transaction</a>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card h-100">
                <div class="card-body">
                    <h5>Loans</h5>
                    <p class="small text-muted">Initiate, update and remove loan requests.</p>
                    <a href="<%= request.getContextPath() %>/loans" class="btn btn-primary btn-sm">View Loans</a>
                    <a href="<%= request.getContextPath() %>/loan" class="btn btn-outline-primary btn-sm">Initiate Loan</a>
                </div>
            </div>
        </div>
    </div>
</div>

<% if (Boolean.TRUE.equals(justLoggedIn)) { %>
<script>
window.addEventListener('load', function(){
    alert('Login successful');
});
</script>
<% } %>
</body>
</html>
