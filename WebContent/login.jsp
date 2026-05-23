<%-- US002: Employee login form --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Login</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h3 class="card-title text-center mb-3">Employee Login</h3>

                    <%-- Show server-side error if any --%>
                    <% String err = (String) request.getAttribute("error");
                       if (err != null) { %>
                        <div class="alert alert-danger"><%= err %></div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/login" method="post" id="loginForm">
                        <div class="mb-3">
                            <label class="form-label">Employee ID</label>
                            <input type="text" name="employeeId" id="employeeId"
                                   class="form-control" required pattern="\d+"
                                   placeholder="7-digit ID like 1000001">
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" name="password" id="password"
                                   class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>

                    <p class="text-center mt-3">
                        New here? <a href="<%= request.getContextPath() %>/register">Register</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Simple client-side check
document.getElementById('loginForm').addEventListener('submit', function(e){
    var id = document.getElementById('employeeId').value;
    var pw = document.getElementById('password').value;
    if (!id || !pw) {
        alert('Please enter both Employee ID and Password.');
        e.preventDefault();
    }
});
</script>
</body>
</html>
