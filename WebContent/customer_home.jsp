<%-- Customer dashboard - shown after a successful customer login --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer" %>
<%
    // Auth check: only show this page to logged-in customers
    Customer cust = (Customer) session.getAttribute("loggedInCustomer");
    if (cust == null) {
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
        return;
    }
    // Show "Login Successful" popup once on first arrival
    Boolean justLoggedIn = (Boolean) session.getAttribute("customerLoginSuccess");
    session.removeAttribute("customerLoginSuccess");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Account - Banking Portal</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">

<%@ include file="/_customer_navbar.jsp" %>

<div class="container py-3">

    <div class="alert alert-success">
        Welcome, <strong><%= cust.getFullName() %></strong>
        (SSN <%= cust.getCustomerSsnId() %>)
    </div>

    <!-- Account summary card -->
    <div class="row g-3 mb-4">
        <div class="col-md-6">
            <div class="card border-success">
                <div class="card-body">
                    <h5 class="card-title">Account Summary</h5>
                    <table class="table table-sm">
                        <tr><th>Account Number</th><td><%= cust.getAccountNumber() %></td></tr>
                        <tr><th>IFSC Code</th><td><%= cust.getIfscCode() %></td></tr>
                        <tr><th>Account Type</th><td><%= cust.getAccountType() %></td></tr>
                        <tr>
                            <th>Available Balance</th>
                            <td>
                                <span class="fs-4 fw-bold text-success">
                                    Rs. <%= String.format("%.2f", cust.getAccountBalance()) %>
                                </span>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="card">
                <div class="card-body">
                    <h5 class="card-title">Personal Details</h5>
                    <table class="table table-sm">
                        <tr><th>Name</th><td><%= cust.getFullName() %></td></tr>
                        <tr><th>Email</th><td><%= cust.getEmail() %></td></tr>
                        <tr><th>Contact</th><td><%= cust.getContactNumber() %></td></tr>
                        <tr><th>Address</th><td><%= cust.getAddress() %></td></tr>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick action tiles -->
    <h5 class="mb-3">What would you like to do?</h5>
    <div class="row g-3">
        <div class="col-md-3">
            <div class="card text-center h-100 shadow-sm">
                <div class="card-body">
                    <h2>&#128176;</h2>
                    <h6>Make Transaction</h6>
                    <p class="small text-muted">Deposit or withdraw money from your account.</p>
                    <a class="btn btn-primary btn-sm w-100"
                       href="<%= request.getContextPath() %>/customer/transaction">Open</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center h-100 shadow-sm">
                <div class="card-body">
                    <h2>&#128179;</h2>
                    <h6>Apply for Loan</h6>
                    <p class="small text-muted">Submit a new loan application.</p>
                    <a class="btn btn-primary btn-sm w-100"
                       href="<%= request.getContextPath() %>/customer/loan">Apply</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center h-100 shadow-sm">
                <div class="card-body">
                    <h2>&#128209;</h2>
                    <h6>My Transactions</h6>
                    <p class="small text-muted">View your transaction history.</p>
                    <a class="btn btn-outline-primary btn-sm w-100"
                       href="<%= request.getContextPath() %>/customer/my-transactions">View</a>
                </div>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card text-center h-100 shadow-sm">
                <div class="card-body">
                    <h2>&#128203;</h2>
                    <h6>My Loans</h6>
                    <p class="small text-muted">Track your loan applications.</p>
                    <a class="btn btn-outline-primary btn-sm w-100"
                       href="<%= request.getContextPath() %>/customer/my-loans">View</a>
                </div>
            </div>
        </div>
    </div>
</div>

<% if (Boolean.TRUE.equals(justLoggedIn)) { %>
<script>
// Show the "Login Successful" popup the first time the dashboard loads
window.addEventListener('load', function(){
    alert('Login Successful');
});
</script>
<% } %>
</body>
</html>
