<%-- Customer makes a transaction (deposit / withdraw) on their own account. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Customer, com.bank.model.Transaction" %>
<%
    // Auth check
    Customer cust = (Customer) session.getAttribute("loggedInCustomer");
    if (cust == null) {
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
        return;
    }
    // Pull any servlet-set attributes
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    Transaction saved = (Transaction) request.getAttribute("savedTransaction");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Make Transaction</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/_customer_navbar.jsp" %>

<div class="container py-3">
    <h3>Make Transaction</h3>
    <p class="text-muted">
        Account: <strong><%= cust.getAccountNumber() %></strong> |
        Current balance: <strong>Rs. <%= String.format("%.2f", cust.getAccountBalance()) %></strong>
    </p>

    <% if (error   != null) { %><div class="alert alert-danger"><%= error %></div><% } %>
    <% if (success != null) { %><div class="alert alert-success"><%= success %></div><% } %>

    <% if (saved != null) { %>
        <div class="card mb-3">
            <div class="card-body">
                <h5>Receipt</h5>
                <table class="table table-sm">
                    <tr><th>Transaction ID</th><td><%= saved.getTransactionId() %></td></tr>
                    <tr><th>Date</th>           <td><%= saved.getDate() %></td></tr>
                    <tr><th>Mode</th>           <td><%= saved.getModeOfTransaction() %></td></tr>
                    <tr><th>Type</th>           <td><%= saved.getCreditDebit() %></td></tr>
                    <tr><th>Amount</th>         <td>Rs. <%= saved.getAmount() %></td></tr>
                    <tr><th>New Balance</th>    <td>Rs. <%= saved.getAccountBalance() %></td></tr>
                </table>
            </div>
        </div>
    <% } %>

    <form action="<%= request.getContextPath() %>/customer/transaction" method="post" class="row g-3">
        <div class="col-md-4">
            <label class="form-label">Date</label>
            <input type="date" name="date" class="form-control"
                   value="" placeholder="(today if blank)">
        </div>
        <div class="col-md-4">
            <label class="form-label">Mode of Transaction *</label>
            <select name="modeOfTransaction" class="form-select" required>
                <option>CASH</option>
                <option>CHEQUE</option>
                <option>ATM</option>
                <option>ONLINE</option>
            </select>
        </div>
        <div class="col-md-4">
            <label class="form-label">Type *</label>
            <select name="creditDebit" class="form-select" required>
                <option value="">Select</option>
                <option value="Credit">Credit (Deposit)</option>
                <option value="Debit">Debit (Withdraw)</option>
            </select>
        </div>

        <div class="col-md-4">
            <label class="form-label">Amount (Rs.) *</label>
            <input type="number" step="0.01" min="0.01" name="amount" class="form-control" required>
        </div>

        <div class="col-12">
            <button type="submit" class="btn btn-success">Submit Transaction</button>
            <a href="<%= request.getContextPath() %>/customer_home.jsp" class="btn btn-secondary">Back to Dashboard</a>
        </div>
    </form>
</div>
</body>
</html>
