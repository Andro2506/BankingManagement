<%-- Customer applies for a loan. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer, com.bank.model.Loan" %>
<%
    // Auth check
    Customer cust = (Customer) session.getAttribute("loggedInCustomer");
    if (cust == null) {
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
        return;
    }
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    Loan saved = (Loan) request.getAttribute("savedLoan");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Apply for Loan</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/_customer_navbar.jsp" %>

<div class="container py-3">
    <h3>Apply for a Loan</h3>
    <p class="text-muted">
        Applicant: <strong><%= cust.getFullName() %></strong> (SSN <%= cust.getCustomerSsnId() %>)
    </p>

    <% if (error   != null) { %><div class="alert alert-danger"><%= error %></div><% } %>
    <% if (success != null) { %><div class="alert alert-success"><%= success %></div><% } %>

    <% if (saved != null) { %>
        <div class="card mb-3">
            <div class="card-body">
                <h5>Application Saved</h5>
                <table class="table table-sm">
                    <tr><th>Loan ID</th>     <td><%= saved.getLoanId() %></td></tr>
                    <tr><th>Type</th>        <td><%= saved.getLoanType() %></td></tr>
                    <tr><th>Amount</th>      <td>Rs. <%= saved.getLoanAmount() %></td></tr>
                    <tr><th>Length</th>      <td><%= saved.getLengthOfLoan() %> months</td></tr>
                    <tr><th>Status</th>      <td><span class="badge bg-warning"><%= saved.getStatus() %></span></td></tr>
                </table>
            </div>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/customer/loan" method="post" class="row g-3">
        <div class="col-md-6">
            <label class="form-label">Loan Type *</label>
            <select name="loanType" class="form-select" required>
                <option value="">Select</option>
                <option>Personal</option>
                <option>Home</option>
                <option>Car</option>
                <option>Education</option>
                <option>Business</option>
            </select>
        </div>
        <div class="col-md-6">
            <label class="form-label">Loan Amount (Rs.) *</label>
            <input type="number" step="0.01" min="1" name="loanAmount" class="form-control" required>
        </div>

        <div class="col-md-6">
            <label class="form-label">Length of Loan (months) *</label>
            <input type="number" min="1" name="lengthOfLoan" class="form-control" required>
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-success">Submit Application</button>
            <a href="<%= request.getContextPath() %>/customer_home.jsp" class="btn btn-secondary">Back to Dashboard</a>
        </div>
    </form>
</div>
</body>
</html>
