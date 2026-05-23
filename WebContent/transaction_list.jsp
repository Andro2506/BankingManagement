<%-- List all transactions --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Transaction, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Transaction> list = (List<Transaction>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transactions</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>Transactions</h3>
        <a href="<%= request.getContextPath() %>/transaction" class="btn btn-primary">+ New</a>
    </div>
    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th><th>Date</th><th>SSN</th><th>Customer</th>
                    <th>Account</th><th>Mode</th><th>Type</th><th>Amount</th><th>Balance</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        Transaction t = list.get(i);
            %>
                <tr>
                    <td><%= t.getTransactionId() %></td>
                    <td><%= t.getDate() %></td>
                    <td><%= t.getCustomerSsnId() %></td>
                    <td><%= t.getCustomerName() %></td>
                    <td><%= t.getAccountNumber() %></td>
                    <td><%= t.getModeOfTransaction() %></td>
                    <td><%= t.getCreditDebit() %></td>
                    <td><%= t.getAmount() %></td>
                    <td><%= t.getAccountBalance() %></td>
                </tr>
            <%  }
                } else { %>
                <tr><td colspan="9" class="text-center">No transactions yet.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
