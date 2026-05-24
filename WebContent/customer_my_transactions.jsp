<%-- Customer's own transaction history --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Customer, com.bank.model.Transaction" %>
<%
    Customer cust = (Customer) session.getAttribute("loggedInCustomer");
    if (cust == null) {
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
        return;
    }
    List<Transaction> list = (List<Transaction>) request.getAttribute("transactions");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Transactions</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/_customer_navbar.jsp" %>

<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>My Transactions</h3>
        <a href="<%= request.getContextPath() %>/customer/transaction" class="btn btn-success">+ New Transaction</a>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>ID</th><th>Date</th><th>Mode</th>
                    <th>Type</th><th>Amount</th><th>Balance After</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (list != null && !list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        Transaction t = list.get(i);
                        // Color the type cell green for credit, red for debit
                        String css = "Credit".equalsIgnoreCase(t.getCreditDebit()) ? "success" : "danger";
            %>
                <tr>
                    <td><%= t.getTransactionId() %></td>
                    <td><%= t.getDate() %></td>
                    <td><%= t.getModeOfTransaction() %></td>
                    <td><span class="badge bg-<%= css %>"><%= t.getCreditDebit() %></span></td>
                    <td>Rs. <%= t.getAmount() %></td>
                    <td>Rs. <%= t.getAccountBalance() %></td>
                </tr>
            <%  }
                } else { %>
                <tr><td colspan="6" class="text-center">You have no transactions yet.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
