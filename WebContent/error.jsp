<%-- Shared error page --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-5">
    <div class="card border-danger">
        <div class="card-header bg-danger text-white">
            <h3 class="mb-0">Something went wrong</h3>
        </div>
        <div class="card-body">
            <% if (exception != null) { %>
                <p><strong><%= exception.getClass().getSimpleName() %>:</strong>
                <%= exception.getMessage() == null ? "" : exception.getMessage() %></p>
            <% } else { %>
                <p>The page could not be processed.</p>
            <% } %>
            <a href="<%= request.getContextPath() %>/" class="btn btn-primary">Back to Home</a>
        </div>
    </div>
</div>
</body>
</html>
