<%-- US007: Loan list with edit/delete actions --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Loan, com.bank.model.Employee" %>
<%
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    List<Loan> loans = (List<Loan>) request.getAttribute("loans");
    String flashMessage = (String) session.getAttribute("flashMessage");
    String flashError   = (String) session.getAttribute("flashError");
    if (flashMessage != null) session.removeAttribute("flashMessage");
    if (flashError   != null) session.removeAttribute("flashError");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Loans</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<%@ include file="/_navbar.jsp" %>
<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>Loans</h3>
        <a href="<%= request.getContextPath() %>/loan" class="btn btn-primary">+ Initiate Loan</a>
    </div>

    <% if (flashMessage != null) { %><div class="alert alert-success"><%= flashMessage %></div><% } %>
    <% if (flashError   != null) { %><div class="alert alert-danger"><%= flashError %></div><% } %>

    <table class="table table-striped table-bordered">
        <thead class="table-dark">
            <tr>
                <th>ID</th><th>SSN</th><th>Customer</th><th>Type</th>
                <th>Amount</th><th>Months</th><th>Status</th><th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (loans != null && !loans.isEmpty()) {
                for (int i = 0; i < loans.size(); i++) {
                    Loan l = loans.get(i);
        %>
            <tr>
                <td><%= l.getLoanId() %></td>
                <td><%= l.getCustomerSsnId() %></td>
                <td><%= l.getCustomerName() %></td>
                <td><%= l.getLoanType() %></td>
                <td><%= l.getLoanAmount() %></td>
                <td><%= l.getLengthOfLoan() %></td>
                <td>
                    <% String s = l.getStatus(); String css = "secondary";
                       if ("Approved".equals(s)) css = "success";
                       else if ("Rejected".equals(s)) css = "danger";
                       else if ("Pending".equals(s)) css = "warning"; %>
                    <span class="badge bg-<%= css %>"><%= s %></span>
                </td>
                <td>
                    <a class="btn btn-sm btn-warning"
                       href="<%= request.getContextPath() %>/loan/edit?id=<%= l.getLoanId() %>">Edit</a>
                    <a class="btn btn-sm btn-danger"
                       href="<%= request.getContextPath() %>/loan/delete?id=<%= l.getLoanId() %>"
                       onclick="return confirm('Delete this loan request?');">Delete</a>
                </td>
            </tr>
        <%  }
            } else { %>
            <tr><td colspan="8" class="text-center">No loans yet.</td></tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
