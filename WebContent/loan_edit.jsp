<%-- US007: Edit a loan record. SSN read-only. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Loan, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    Loan loan = (Loan) request.getAttribute("loan");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    if (loan == null) {
        response.sendRedirect(request.getContextPath() + "/loans");
        return;
    }
    String s = loan.getStatus() == null ? "" : loan.getStatus();
    String t = loan.getLoanType() == null ? "" : loan.getLoanType();
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Loan</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <h3>Edit Loan #<%= loan.getLoanId() %></h3>

    <% if (error != null) { %>   <div class="alert alert-danger"><%= error %></div> <% } %>
    <% if (success != null) { %> <div class="alert alert-success"><%= success %></div> <% } %>

    <form action="<%= request.getContextPath() %>/loan/edit" method="post" class="row g-3">
        <input type="hidden" name="loanId" value="<%= loan.getLoanId() %>">

        <div class="col-md-4">
            <label class="form-label">SSN (read-only)</label>
            <input type="text" class="form-control" value="<%= loan.getCustomerSsnId() %>" readonly>
        </div>
        <div class="col-md-4">
            <label class="form-label">Customer Name</label>
            <input type="text" name="customerName" class="form-control"
                   value="<%= loan.getCustomerName() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Loan Type</label>
            <select name="loanType" class="form-select">
                <option <%= "Personal".equals(t) ? "selected" : "" %>>Personal</option>
                <option <%= "Home".equals(t) ? "selected" : "" %>>Home</option>
                <option <%= "Car".equals(t) ? "selected" : "" %>>Car</option>
                <option <%= "Education".equals(t) ? "selected" : "" %>>Education</option>
                <option <%= "Business".equals(t) ? "selected" : "" %>>Business</option>
            </select>
        </div>

        <div class="col-md-4">
            <label class="form-label">Loan Amount</label>
            <input type="number" step="0.01" name="loanAmount" class="form-control"
                   value="<%= loan.getLoanAmount() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Length (months)</label>
            <input type="number" name="lengthOfLoan" class="form-control"
                   value="<%= loan.getLengthOfLoan() %>" required>
        </div>
        <div class="col-md-4">
            <label class="form-label">Status</label>
            <select name="status" class="form-select">
                <option <%= "Pending".equals(s) ? "selected" : "" %>>Pending</option>
                <option <%= "Approved".equals(s) ? "selected" : "" %>>Approved</option>
                <option <%= "Rejected".equals(s) ? "selected" : "" %>>Rejected</option>
            </select>
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-primary">Update</button>
            <a href="<%= request.getContextPath() %>/loans" class="btn btn-secondary">Back</a>
        </div>
    </form>
</div>
</body>
</html>
