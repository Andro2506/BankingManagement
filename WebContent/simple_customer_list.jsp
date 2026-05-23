<%-- Simple Customer list (Servlet US004) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, java.util.Map" %>
<%
    List<Map<String,Object>> rows = (List<Map<String,Object>>) request.getAttribute("rows");
    String flash = (String) request.getAttribute("flash");
    String err   = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Customer List (Servlet)</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-3">
    <div class="d-flex justify-content-between mb-3">
        <h3>Simple Customer (Servlet US004)</h3>
        <a href="<%= request.getContextPath() %>/simpleCustomer?action=new" class="btn btn-primary">+ New</a>
    </div>
    <p><a href="<%= request.getContextPath() %>/index.jsp">Home</a> |
       <a href="<%= request.getContextPath() %>/simple_customer_jsp.jsp">JSP version (US005)</a></p>

    <% if (flash != null) { %><div class="alert alert-success"><%= flash %></div><% } %>
    <% if (err   != null) { %><div class="alert alert-danger"><%= err %></div><% } %>

    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr><th>Account</th><th>Name</th><th>Gender</th><th>DOB</th>
                <th>State</th><th>City</th><th>Type</th><th>Balance</th><th>Actions</th></tr>
        </thead>
        <tbody>
        <%
            if (rows != null && !rows.isEmpty()) {
                for (int i = 0; i < rows.size(); i++) {
                    Map<String,Object> r = rows.get(i);
        %>
            <tr>
                <td><%= r.get("accountNumber") %></td>
                <td><%= r.get("holderName") %></td>
                <td><%= r.get("gender") %></td>
                <td><%= r.get("dob") %></td>
                <td><%= r.get("state") %></td>
                <td><%= r.get("city") %></td>
                <td><%= r.get("accountType") %></td>
                <td><%= r.get("balance") %></td>
                <td>
                    <a class="btn btn-sm btn-warning"
                       href="<%= request.getContextPath() %>/simpleCustomer?action=edit&accountNumber=<%= r.get("accountNumber") %>">Edit</a>
                    <a class="btn btn-sm btn-danger"
                       href="<%= request.getContextPath() %>/simpleCustomer?action=delete&accountNumber=<%= r.get("accountNumber") %>"
                       onclick="return confirm('Delete this row?');">Delete</a>
                </td>
            </tr>
        <%  }
            } else { %>
            <tr><td colspan="9" class="text-center">No rows yet.</td></tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
