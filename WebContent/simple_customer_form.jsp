<%-- Simple Customer Registration form (Servlet US004) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%
    Map<String,Object> row = (Map<String,Object>) request.getAttribute("row");
    String error = (String) request.getAttribute("error");

    String acc   = row != null ? String.valueOf(row.get("accountNumber")) : "";
    String name  = row != null ? String.valueOf(row.get("holderName")) : "";
    String gen   = row != null ? String.valueOf(row.get("gender")) : "";
    String dob   = row != null ? String.valueOf(row.get("dob")) : "";
    String stt   = row != null ? String.valueOf(row.get("state")) : "";
    String city  = row != null ? String.valueOf(row.get("city")) : "";
    String type  = row != null ? String.valueOf(row.get("accountType")) : "";
    String bal   = row != null ? String.valueOf(row.get("balance")) : "0";
    boolean readOnly = row != null;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Customer Form (Servlet)</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-3">
    <h3><%= readOnly ? "Edit" : "New" %> Simple Customer (Servlet)</h3>
    <p><a href="<%= request.getContextPath() %>/simpleCustomer">Back to list</a></p>

    <% if (error != null) { %><div class="alert alert-danger"><%= error %></div><% } %>

    <form method="post" action="<%= request.getContextPath() %>/simpleCustomer">
        <input type="hidden" name="action" value="save">
        <div class="row g-3">
            <div class="col-md-4">
                <label class="form-label">Account Number *</label>
                <input type="text" name="accountNumber" class="form-control"
                       value="<%= acc %>" <%= readOnly ? "readonly" : "" %> required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Account Holder Name *</label>
                <input type="text" name="holderName" class="form-control" value="<%= name %>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Gender *</label>
                <select name="gender" class="form-select" required>
                    <option value="M" <%= "M".equals(gen) ? "selected" : "" %>>M</option>
                    <option value="F" <%= "F".equals(gen) ? "selected" : "" %>>F</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">DOB *</label>
                <input type="date" name="dob" class="form-control" value="<%= dob %>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">State *</label>
                <input type="text" name="state" class="form-control" value="<%= stt %>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">City *</label>
                <input type="text" name="city" class="form-control" value="<%= city %>" required>
            </div>
            <div class="col-md-4">
                <label class="form-label">Account Type *</label>
                <select name="accountType" class="form-select" required>
                    <option <%= "Current".equals(type) ? "selected" : "" %>>Current</option>
                    <option <%= "Savings".equals(type) ? "selected" : "" %>>Savings</option>
                </select>
            </div>
            <div class="col-md-4">
                <label class="form-label">Balance *</label>
                <input type="number" step="0.01" name="balance" class="form-control"
                       value="<%= bal %>" required>
            </div>
            <div class="col-12">
                <button type="submit" class="btn btn-primary">Save</button>
            </div>
        </div>
    </form>
</div>
</body>
</html>
