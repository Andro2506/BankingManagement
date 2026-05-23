<%-- US003: Customer registration form (employee creates a customer) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    Customer saved = (Customer) request.getAttribute("registeredCustomer");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Register Customer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>

<div class="container py-3">
    <h3>Register New Customer</h3>

    <% if (error != null) { %>
        <div class="alert alert-danger"><%= error %></div>
    <% } %>
    <% if (success != null) { %>
        <div class="alert alert-success"><%= success %></div>
    <% } %>

    <form action="<%= request.getContextPath() %>/customer/register" method="post" class="row g-3">
        <div class="col-md-4">
            <label class="form-label">Customer SSN ID * (7 digits)</label>
            <input type="text" name="customerSsnId" class="form-control" pattern="\d{7}" maxlength="7" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">First Name *</label>
            <input type="text" name="firstName" class="form-control" maxlength="50" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Last Name *</label>
            <input type="text" name="lastName" class="form-control" maxlength="50" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Account Number *</label>
            <input type="text" name="accountNumber" class="form-control" maxlength="20" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">IFSC Code *</label>
            <input type="text" name="ifscCode" class="form-control" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Account Balance *</label>
            <input type="number" step="0.01" name="accountBalance" class="form-control" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Aadhar Card No * (12 digits)</label>
            <input type="text" name="aadharNumber" class="form-control" pattern="\d{12}" maxlength="12" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">PAN Card No * (10 chars)</label>
            <input type="text" name="panNumber" class="form-control" maxlength="10" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Date of Birth *</label>
            <input type="date" name="dateOfBirth" class="form-control" required>
        </div>

        <div class="col-md-3">
            <label class="form-label">Gender *</label>
            <select name="gender" class="form-select" required>
                <option value="">Select</option>
                <option value="M">M</option>
                <option value="F">F</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Marital Status *</label>
            <select name="maritalStatus" class="form-select" required>
                <option value="">Select</option>
                <option>Single</option>
                <option>Married</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Account Type *</label>
            <select name="accountType" class="form-select" required>
                <option value="">Select</option>
                <option>Current</option>
                <option>Savings</option>
                <option>Salary</option>
                <option>Joint</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Contact Number * (10 digits)</label>
            <input type="text" name="contactNumber" class="form-control" pattern="\d{10}" maxlength="10" required>
        </div>

        <div class="col-md-6">
            <label class="form-label">Email *</label>
            <input type="email" name="email" class="form-control" required>
        </div>
        <div class="col-md-6">
            <label class="form-label">Address *</label>
            <input type="text" name="address" class="form-control" maxlength="100" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Occupation</label>
            <input type="text" name="occupation" class="form-control">
        </div>
        <div class="col-md-4">
            <label class="form-label">Employer Name</label>
            <input type="text" name="employerName" class="form-control">
        </div>
        <div class="col-md-4">
            <label class="form-label">Employer Address</label>
            <input type="text" name="employerAddress" class="form-control">
        </div>

        <div class="col-md-4">
            <label class="form-label">Customer Password</label>
            <input type="text" name="password" class="form-control" placeholder="Default: cust123">
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-success">Register Customer</button>
            <a href="<%= request.getContextPath() %>/customers" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<% if (success != null) { %>
<script>
window.addEventListener('load', function(){
    alert('<%= success %>');
});
</script>
<% } %>
</body>
</html>
