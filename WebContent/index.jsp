<%-- index.jsp - landing page; redirects users to login if not signed in. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // If an employee is already logged in, jump straight to the home page.
    if (session != null && session.getAttribute("loggedInEmployee") != null) {
        response.sendRedirect(request.getContextPath() + "/home.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Banking Management System</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="text-center mb-4">
        <h1 class="display-5 text-primary">Banking Management System</h1>
        <p class="text-muted">Employee portal - register, login and manage customers, transactions and loans.</p>
    </div>
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <h3 class="card-title">Welcome</h3>
                    <p class="card-text">Please choose an action to continue.</p>
                    <a href="<%= request.getContextPath() %>/login" class="btn btn-primary me-2">Employee Login</a>
                    <a href="<%= request.getContextPath() %>/register" class="btn btn-outline-primary">New Employee Register</a>
                </div>
            </div>

            <div class="mt-4 card">
                <div class="card-body">
                    <h5>Other modules</h5>
                    <ul class="list-unstyled mb-0">
                        <li><a href="<%= request.getContextPath() %>/netbanking_login.html">NetBanking Login (Servlet)</a></li>
                        <li><a href="<%= request.getContextPath() %>/netbanking_login_jsp.jsp">NetBanking Login (JSP)</a></li>
                        <li><a href="<%= request.getContextPath() %>/customer_login.jsp">Customer Login (DB)</a></li>
                        <li><a href="<%= request.getContextPath() %>/simpleCustomer">Simple Customer CRUD (Servlet)</a></li>
                        <li><a href="<%= request.getContextPath() %>/simple_customer_jsp.jsp">Simple Customer CRUD (JSP)</a></li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
