<%-- US004: Edit customer details. SSN is read-only. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Customer c = (Customer) request.getAttribute("customer");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    if (c == null) {
        response.sendRedirect(request.getContextPath() + "/customers");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Customer</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <h3>Edit Customer</h3>

    <% if (error != null) { %>   <div class="alert alert-danger"><%= error %></div> <% } %>
    <% if (success != null) { %> <div class="alert alert-success"><%= success %></div> <% } %>

    <form action="<%= request.getContextPath() %>/customer/edit" method="post" class="row g-3">
        <div class="col-md-4">
            <label class="form-label">SSN ID (cannot be changed)</label>
            <input type="text" name="customerSsnId" value="<%= c.getCustomerSsnId() %>"
                   class="form-control" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label">First Name</label>
            <input type="text" name="firstName" class="form-control" value="<%= c.getFirstName() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Last Name</label>
            <input type="text" name="lastName" class="form-control" value="<%= c.getLastName() %>" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" value="<%= c.getEmail() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Date of Birth</label>
            <input type="date" name="dateOfBirth" class="form-control" value="<%= c.getDateOfBirth() %>">
        </div>
        <div class="col-md-4">
            <label class="form-label">Contact Number</label>
            <input type="text" name="contactNumber" class="form-control" pattern="\d{10}" maxlength="10"
                   value="<%= c.getContactNumber() %>" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Aadhar No (12 digits)</label>
            <input type="text" name="aadharNumber" class="form-control" pattern="\d{12}" maxlength="12"
                   value="<%= c.getAadharNumber() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">PAN No</label>
            <input type="text" name="panNumber" class="form-control" maxlength="10"
                   value="<%= c.getPanNumber() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Address</label>
            <input type="text" name="address" class="form-control" maxlength="100"
                   value="<%= c.getAddress() %>" required>
        </div>

        <div class="col-md-4">
            <label class="form-label">Account Number</label>
            <input type="text" name="accountNumber" class="form-control"
                   value="<%= c.getAccountNumber() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">IFSC Code</label>
            <input type="text" name="ifscCode" class="form-control"
                   value="<%= c.getIfscCode() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Account Balance</label>
            <input type="number" step="0.01" name="accountBalance" class="form-control"
                   value="<%= c.getAccountBalance() %>" required>
        </div>

        <div class="col-md-3">
            <label class="form-label">Account Type</label>
            <select name="accountType" class="form-select">
                <% String at = c.getAccountType() == null ? "" : c.getAccountType(); %>
                <option <%= "Current".equals(at) ? "selected" : "" %>>Current</option>
                <option <%= "Savings".equals(at) ? "selected" : "" %>>Savings</option>
                <option <%= "Salary".equals(at) ? "selected" : "" %>>Salary</option>
                <option <%= "Joint".equals(at) ? "selected" : "" %>>Joint</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Gender</label>
            <select name="gender" class="form-select">
                <% String g = c.getGender() == null ? "" : c.getGender(); %>
                <option value="M" <%= "M".equals(g) ? "selected" : "" %>>M</option>
                <option value="F" <%= "F".equals(g) ? "selected" : "" %>>F</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Marital Status</label>
            <select name="maritalStatus" class="form-select">
                <% String ms = c.getMaritalStatus() == null ? "" : c.getMaritalStatus(); %>
                <option <%= "Single".equals(ms) ? "selected" : "" %>>Single</option>
                <option <%= "Married".equals(ms) ? "selected" : "" %>>Married</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Occupation</label>
            <input type="text" name="occupation" class="form-control"
                   value="<%= c.getOccupation() == null ? "" : c.getOccupation() %>">
        </div>

        <div class="col-md-6">
            <label class="form-label">Employer Name</label>
            <input type="text" name="employerName" class="form-control"
                   value="<%= c.getEmployerName() == null ? "" : c.getEmployerName() %>">
        </div>
        <div class="col-md-6">
            <label class="form-label">Employer Address</label>
            <input type="text" name="employerAddress" class="form-control"
                   value="<%= c.getEmployerAddress() == null ? "" : c.getEmployerAddress() %>">
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-primary">Update</button>
            <a href="<%= request.getContextPath() %>/customers" class="btn btn-secondary">Back</a>
        </div>
    </form>
</div>
</body>
</html>
