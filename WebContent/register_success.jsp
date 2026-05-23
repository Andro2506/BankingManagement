<%-- Acknowledgment page after successful employee registration --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bank.model.Employee" %>
<%
    Employee emp = (Employee) request.getAttribute("registeredEmployee");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Successful</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card border-success">
                <div class="card-header bg-success text-white text-center">
                    <h4>Employee Registration Successful</h4>
                </div>
                <div class="card-body">
                    <% if (emp != null) { %>
                        <table class="table">
                            <tr><th>Employee ID</th><td><%= emp.getEmployeeId() %></td></tr>
                            <tr><th>Name</th><td><%= emp.getFullName() %></td></tr>
                            <tr><th>Email</th><td><%= emp.getEmail() %></td></tr>
                        </table>
                        <div class="alert alert-info">
                            Please remember your <strong>Employee ID</strong>; you will use it to log in.
                        </div>
                    <% } else { %>
                        <p>No registration data available.</p>
                    <% } %>
                    <a href="<%= request.getContextPath() %>/login" class="btn btn-primary">Go to Login</a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Show a popup confirmation
window.addEventListener('load', function(){
    alert('Employee Registration Successful');
});
</script>
</body>
</html>
