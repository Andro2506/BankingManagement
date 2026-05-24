<%-- Manager-only page: shows total employee count and a table with a
     "change designation" form on each row. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, com.bank.model.Employee" %>
<%
    // Auth check (also done by the servlet, but JSP guards in case of direct hit)
    Employee emp = (Employee) session.getAttribute("loggedInEmployee");
    if (emp == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    if (!"Manager".equalsIgnoreCase(emp.getDesignation())) {
        response.sendRedirect(request.getContextPath() + "/home.jsp");
        return;
    }

    // Read attributes set by EmployeeListServlet
    List<Employee> employees = (List<Employee>) request.getAttribute("employees");
    Integer total = (Integer) request.getAttribute("totalEmployees");

    // Pull and clear flash messages from the session
    String flashMessage = (String) session.getAttribute("flashMessage");
    String flashError   = (String) session.getAttribute("flashError");
    if (flashMessage != null) session.removeAttribute("flashMessage");
    if (flashError   != null) session.removeAttribute("flashError");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Manage Employees</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body class="bg-light">

<%@ include file="/_navbar.jsp" %>

<div class="container py-3">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3>Employees</h3>
        <span class="badge bg-primary fs-6">
            Total: <%= total != null ? total : 0 %>
        </span>
    </div>

    <% if (flashMessage != null) { %>
        <div class="alert alert-success"><%= flashMessage %></div>
    <% } %>
    <% if (flashError != null) { %>
        <div class="alert alert-danger"><%= flashError %></div>
    <% } %>

    <p class="text-muted small">
        As a Manager you can change any employee's designation below.
        You cannot demote yourself.
    </p>

    <div class="table-responsive">
        <table class="table table-striped table-bordered align-middle">
            <thead class="table-dark">
                <tr>
                    <th>Employee ID</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Contact</th>
                    <th>Current Designation</th>
                    <th>Salary</th>
                    <th>Change Designation</th>
                </tr>
            </thead>
            <tbody>
            <%
                if (employees != null && !employees.isEmpty()) {
                    for (int i = 0; i < employees.size(); i++) {
                        Employee row = employees.get(i);
                        boolean isMe = (row.getEmployeeId() == emp.getEmployeeId());
                        String d = row.getDesignation() == null ? "" : row.getDesignation();
            %>
                <tr <%= isMe ? "class='table-warning'" : "" %>>
                    <td><%= row.getEmployeeId() %><%= isMe ? " <small>(you)</small>" : "" %></td>
                    <td><%= row.getFullName() %></td>
                    <td><%= row.getEmail() %></td>
                    <td><%= row.getContactNumber() %></td>
                    <td>
                        <%
                            // Color-code the badge for quick visual grouping
                            String css = "secondary";
                            if ("Manager".equals(d))         css = "primary";
                            else if ("Accountant".equals(d)) css = "info";
                            else if ("Clerk".equals(d))      css = "success";
                        %>
                        <span class="badge bg-<%= css %>"><%= d %></span>
                    </td>
                    <td>Rs. <%= String.format("%.2f", row.getSalary()) %></td>
                    <td>
                        <form action="<%= request.getContextPath() %>/employee/edit-designation"
                              method="post" class="d-flex gap-1">
                            <input type="hidden" name="employeeId" value="<%= row.getEmployeeId() %>">
                            <select name="designation" class="form-select form-select-sm">
                                <option value="Clerk"      <%= "Clerk".equals(d)      ? "selected" : "" %>>Clerk</option>
                                <option value="Manager"    <%= "Manager".equals(d)    ? "selected" : "" %>>Manager</option>
                                <option value="Accountant" <%= "Accountant".equals(d) ? "selected" : "" %>>Accountant</option>
                            </select>
                            <button type="submit" class="btn btn-sm btn-warning">Save</button>
                        </form>
                    </td>
                </tr>
            <%
                    }
                } else {
            %>
                <tr><td colspan="7" class="text-center">No employees yet.</td></tr>
            <% } %>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>
