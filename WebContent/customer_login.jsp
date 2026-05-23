<%-- Customer Login form (Servlet US002 - posts to /customerLogin servlet) --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Customer Login (DB)</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title text-center">Customer Login (DB)</h3>
                    <p class="text-muted text-center small">
                        Username = customer SSN id (from customer table).
                        Default password for demo records is <code>cust123</code>.
                    </p>
                    <form action="<%= request.getContextPath() %>/customerLogin" method="post">
                        <div class="mb-3">
                            <label class="form-label">Username (SSN)</label>
                            <input type="text" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <button type="submit" class="btn btn-primary w-100">Login</button>
                    </form>
                </div>
            </div>
            <p class="text-center mt-3"><a href="index.jsp">Home</a></p>
        </div>
    </div>
</div>
</body>
</html>
