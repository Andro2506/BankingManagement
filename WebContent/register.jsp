<%-- US001: Employee registration form --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Employee Registration</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-7">
            <div class="card shadow-sm">
                <div class="card-body">
                    <h3 class="card-title text-center mb-3">Employee Registration</h3>

                    <% String err = (String) request.getAttribute("error");
                       if (err != null) { %>
                        <div class="alert alert-danger"><%= err %></div>
                    <% } %>

                    <form action="<%= request.getContextPath() %>/register" method="post" id="regForm">
                        <div class="mb-3">
                            <label class="form-label">Employee ID</label>
                            <input type="text" class="form-control" value="(auto-generated 7-digit)" disabled>
                            <small class="text-muted">A 7-digit Employee ID will be generated automatically.</small>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">First Name *</label>
                                <input type="text" name="firstName" class="form-control" maxlength="50" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Last Name *</label>
                                <input type="text" name="lastName" class="form-control" maxlength="50" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Email *</label>
                            <input type="email" name="email" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Contact Number * (10 digits)</label>
                            <input type="text" name="contactNumber" class="form-control"
                                   pattern="\d{10}" maxlength="10" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Address *</label>
                            <textarea name="address" class="form-control" maxlength="100" required></textarea>
                        </div>
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Password *</label>
                                <input type="password" name="password" id="password"
                                       class="form-control" minlength="4" maxlength="30" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label class="form-label">Confirm Password *</label>
                                <input type="password" name="confirmPassword" id="confirmPassword"
                                       class="form-control" required>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Register</button>
                    </form>

                    <p class="text-center mt-3">
                        Already registered? <a href="<%= request.getContextPath() %>/login">Login</a>
                    </p>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
// Client-side validation: passwords must match
document.getElementById('regForm').addEventListener('submit', function(e){
    var p = document.getElementById('password').value;
    var c = document.getElementById('confirmPassword').value;
    if (p !== c) {
        alert('Password and Confirm Password do not match.');
        e.preventDefault();
    }
});
</script>
</body>
</html>
