<%-- US006: Capture transaction info --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Employee, com.bank.model.Transaction" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    Transaction saved = (Transaction) request.getAttribute("savedTransaction");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Record Transaction</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <h3>Record New Transaction</h3>

    <% if (error != null) { %>   <div class="alert alert-danger"><%= error %></div> <% } %>
    <% if (success != null) { %> <div class="alert alert-success"><%= success %></div> <% } %>

    <% if (saved != null) { %>
        <div class="card mb-3">
            <div class="card-body">
                <h5>Transaction recorded</h5>
                <p>ID: <%= saved.getTransactionId() %> | <%= saved.getCreditDebit() %> Rs. <%= saved.getAmount() %>
                   for <%= saved.getCustomerName() %> | New balance: Rs. <%= saved.getAccountBalance() %></p>
            </div>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/transaction" method="post" class="row g-3">
        <div class="col-md-3">
            <label class="form-label">Customer SSN ID *</label>
            <input type="text" name="customerSsnId" class="form-control" pattern="\d{7}" maxlength="7" required>
            <small class="text-muted">Other fields are looked up automatically.</small>
        </div>
        <div class="col-md-3">
            <label class="form-label">Date</label>
            <input type="date" name="date" class="form-control">
        </div>
        <div class="col-md-3">
            <label class="form-label">Mode of Transaction *</label>
            <select name="modeOfTransaction" class="form-select" required>
                <option>CASH</option>
                <option>CHEQUE</option>
                <option>ATM</option>
                <option>ONLINE</option>
            </select>
        </div>
        <div class="col-md-3">
            <label class="form-label">Credit / Debit *</label>
            <select name="creditDebit" class="form-select" required>
                <option value="">Select</option>
                <option value="Credit">Credit</option>
                <option value="Debit">Debit</option>
            </select>
        </div>

        <div class="col-md-4">
            <label class="form-label">Amount *</label>
            <input type="number" step="0.01" min="0.01" name="amount" class="form-control" required>
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-success">Record Transaction</button>
            <a href="<%= request.getContextPath() %>/transactions" class="btn btn-secondary">View All</a>
        </div>
    </form>
</div>
</body>
</html>
