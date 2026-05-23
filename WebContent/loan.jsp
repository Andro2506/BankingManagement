<%-- US006: Initiate a loan request --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Employee, com.bank.model.Loan" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
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
    <title>Initiate Loan</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <h3>Initiate Loan Request</h3>

    <% if (error != null) { %>   <div class="alert alert-danger"><%= error %></div> <% } %>
    <% if (success != null) { %> <div class="alert alert-success"><%= success %></div> <% } %>

    <form action="<%= request.getContextPath() %>/loan" method="post" class="row g-3">
        <div class="col-md-4">
            <label class="form-label">Customer SSN ID *</label>
            <input type="text" name="customerSsnId" class="form-control" pattern="\d{7}" maxlength="7" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Customer Name</label>
            <input type="text" name="customerName" class="form-control" placeholder="(auto-fill if blank)">
        </div>
        <div class="col-md-4">
            <label class="form-label">Loan Type *</label>
            <select name="loanType" class="form-select" required>
                <option>Personal</option>
                <option>Home</option>
                <option>Car</option>
                <option>Education</option>
                <option>Business</option>
            </select>
        </div>

        <div class="col-md-6">
            <label class="form-label">Loan Amount *</label>
            <input type="number" step="0.01" min="1" name="loanAmount" class="form-control" required>
        </div>
        <div class="col-md-6">
            <label class="form-label">Length of Loan (months) *</label>
            <input type="number" min="1" name="lengthOfLoan" class="form-control" required>
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-success">Submit Loan Request</button>
            <a href="<%= request.getContextPath() %>/loans" class="btn btn-secondary">View All</a>
        </div>
    </form>
</div>
</body>
</html>
