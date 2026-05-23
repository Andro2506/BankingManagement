<%-- NetBanking Login (US003 from Servlet sheet) - same as the Servlet version,
     but the password check is done in this JSP using <% Java code %> blocks. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    final String PREDEFINED = "netbank123";
    String entered = request.getParameter("password");
    String message = null;
    String css = "";
    if (entered != null) {
        if (PREDEFINED.equals(entered)) {
            message = "Login Successful";
            css = "alert-success";
        } else {
            message = "Login Unsuccessful";
            css = "alert-danger";
        }
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>NetBanking Login (JSP)</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-5">
            <div class="card">
                <div class="card-body">
                    <h3 class="card-title text-center">NetBanking Login (JSP)</h3>
                    <p class="text-muted text-center small">
                        Enter the predefined password (<code>netbank123</code>).
                    </p>

                    <% if (message != null) { %>
                        <div class="alert <%= css %>"><%= message %></div>
                    <% } %>

                    <form method="post" action="netbanking_login_jsp.jsp">
                        <div class="mb-3">
                            <label class="form-label">Password</label>
                            <input type="text" name="password" class="form-control" required>
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
