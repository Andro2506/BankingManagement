<%-- Customer's own loan history --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Customer, com.bank.model.Loan" %>
<%
    Customer cust = (Customer) session.getAttribute("loggedInCustomer");
    if (cust == null) {
        response.sendRedirect(request.getContextPath() + "/customer_login.jsp");
        return;
    }
    List<Loan> loans = (List<Loan>) request.getAttribute("loans");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Loans</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">

<%@ include file="/_customer_navbar.jsp" %>

<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>My Loan Applications</h3>
        <a href="<%= request.getContextPath() %>/customer/loan" class="btn btn-success">+ Apply for Loan</a>
    </div>

    <div class="table-responsive">
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>Loan ID</th><th>Type</th><th>Amount</th>
                    <th>Length (months)</th><th>Status</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (loans != null && !loans.isEmpty()) {
                    for (int i = 0; i < loans.size(); i++) {
                        Loan l = loans.get(i);
                        // Color the status badge based on its value
                        String s = l.getStatus();
                        String css = "secondary";
                        if ("Approved".equals(s)) css = "success";
                        else if ("Rejected".equals(s)) css = "danger";
                        else if ("Pending".equals(s))  css = "warning";
            %>
                <tr>
                    <td><%= l.getLoanId() %></td>
                    <td><%= l.getLoanType() %></td>
                    <td>Rs. <%= l.getLoanAmount() %></td>
                    <td><%= l.getLengthOfLoan() %></td>
                    <td><span class="badge bg-<%= css %>"><%= s %></span></td>
                </tr>
            <%  }
                } else { %>
                <tr><td colspan="5" class="text-center">You have no loan applications yet.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
