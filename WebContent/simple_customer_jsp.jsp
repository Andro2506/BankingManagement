<%-- Customer Registration - JSP (US005) - MVC CRUD using only this JSP file. --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*, java.util.*, com.bank.util.DBConnection" %>
<%
    // -------- Make sure the table exists --------
    Connection con = null;
    try {
        con = DBConnection.getConnection();
        Statement createSt = con.createStatement();
        createSt.execute(
            "CREATE TABLE IF NOT EXISTS simple_customer_jsp (" +
            "  account_number TEXT PRIMARY KEY," +
            "  holder_name TEXT, gender TEXT, dob TEXT, state TEXT, city TEXT," +
            "  account_type TEXT, balance REAL)");
        createSt.close();

        // -------- Handle action --------
        String action = request.getParameter("action");
        String flash = null;
        String err = null;

        if ("save".equals(action)) {
            String acc = request.getParameter("accountNumber");
            String holder = request.getParameter("holderName");
            String gender = request.getParameter("gender");
            String dob = request.getParameter("dob");
            String state = request.getParameter("state");
            String city = request.getParameter("city");
            String type = request.getParameter("accountType");
            double balance = 0.0;
            try { balance = Double.parseDouble(request.getParameter("balance")); }
            catch (Exception ex) { err = "Balance must be a number."; }

            if (err == null) {
                // Upsert: check existence
                PreparedStatement chk = con.prepareStatement(
                    "SELECT 1 FROM simple_customer_jsp WHERE account_number = ?");
                chk.setString(1, acc);
                ResultSet rs = chk.executeQuery();
                boolean exists = rs.next();
                rs.close(); chk.close();

                if (exists) {
                    PreparedStatement up = con.prepareStatement(
                        "UPDATE simple_customer_jsp SET holder_name=?, gender=?, dob=?, " +
                        "state=?, city=?, account_type=?, balance=? WHERE account_number=?");
                    up.setString(1, holder); up.setString(2, gender); up.setString(3, dob);
                    up.setString(4, state); up.setString(5, city); up.setString(6, type);
                    up.setDouble(7, balance); up.setString(8, acc);
                    up.executeUpdate(); up.close();
                    flash = "Updated " + acc;
                } else {
                    PreparedStatement ins = con.prepareStatement(
                        "INSERT INTO simple_customer_jsp VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    ins.setString(1, acc); ins.setString(2, holder); ins.setString(3, gender);
                    ins.setString(4, dob); ins.setString(5, state); ins.setString(6, city);
                    ins.setString(7, type); ins.setDouble(8, balance);
                    ins.executeUpdate(); ins.close();
                    flash = "Inserted " + acc;
                }
            }
        } else if ("delete".equals(action)) {
            String acc = request.getParameter("accountNumber");
            PreparedStatement del = con.prepareStatement(
                "DELETE FROM simple_customer_jsp WHERE account_number = ?");
            del.setString(1, acc);
            int n = del.executeUpdate();
            del.close();
            flash = (n > 0) ? "Deleted " + acc : "Nothing to delete";
        }

        // For "edit" we just preload values to display in the form.
        Map<String,Object> editRow = null;
        if ("edit".equals(action)) {
            String acc = request.getParameter("accountNumber");
            PreparedStatement sel = con.prepareStatement(
                "SELECT * FROM simple_customer_jsp WHERE account_number = ?");
            sel.setString(1, acc);
            ResultSet rs = sel.executeQuery();
            if (rs.next()) {
                editRow = new HashMap<String,Object>();
                editRow.put("accountNumber", rs.getString("account_number"));
                editRow.put("holderName", rs.getString("holder_name"));
                editRow.put("gender", rs.getString("gender"));
                editRow.put("dob", rs.getString("dob"));
                editRow.put("state", rs.getString("state"));
                editRow.put("city", rs.getString("city"));
                editRow.put("accountType", rs.getString("account_type"));
                editRow.put("balance", rs.getDouble("balance"));
            }
            rs.close(); sel.close();
        }

        // -------- Output starts here --------
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Simple Customer (JSP US005)</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
</head>
<body class="bg-light">
<div class="container py-3">
    <h3>Simple Customer (JSP US005)</h3>
    <p>
        <a href="<%= request.getContextPath() %>/index.jsp">Home</a> |
        <a href="<%= request.getContextPath() %>/simpleCustomer">Servlet version (US004)</a>
    </p>

    <% if (flash != null) { %><div class="alert alert-success"><%= flash %></div><% } %>
    <% if (err   != null) { %><div class="alert alert-danger"><%= err %></div><% } %>

    <%-- ---- Form (insert or edit) ---- --%>
    <%
        String fAcc = editRow != null ? String.valueOf(editRow.get("accountNumber")) : "";
        String fName = editRow != null ? String.valueOf(editRow.get("holderName")) : "";
        String fGen = editRow != null ? String.valueOf(editRow.get("gender")) : "";
        String fDob = editRow != null ? String.valueOf(editRow.get("dob")) : "";
        String fState = editRow != null ? String.valueOf(editRow.get("state")) : "";
        String fCity = editRow != null ? String.valueOf(editRow.get("city")) : "";
        String fType = editRow != null ? String.valueOf(editRow.get("accountType")) : "";
        String fBal = editRow != null ? String.valueOf(editRow.get("balance")) : "0";
        boolean ro = editRow != null;
    %>
    <div class="card mb-3">
        <div class="card-body">
            <h5><%= ro ? "Edit row" : "New row" %></h5>
            <form method="post" action="simple_customer_jsp.jsp" class="row g-2">
                <input type="hidden" name="action" value="save">
                <div class="col-md-3">
                    <input type="text" name="accountNumber" class="form-control"
                           placeholder="Account #" value="<%= fAcc %>"
                           <%= ro ? "readonly" : "" %> required>
                </div>
                <div class="col-md-3">
                    <input type="text" name="holderName" class="form-control"
                           placeholder="Holder Name" value="<%= fName %>" required>
                </div>
                <div class="col-md-2">
                    <select name="gender" class="form-select" required>
                        <option value="M" <%= "M".equals(fGen) ? "selected" : "" %>>M</option>
                        <option value="F" <%= "F".equals(fGen) ? "selected" : "" %>>F</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <input type="date" name="dob" class="form-control" value="<%= fDob %>" required>
                </div>
                <div class="col-md-2">
                    <input type="text" name="state" class="form-control"
                           placeholder="State" value="<%= fState %>" required>
                </div>
                <div class="col-md-3">
                    <input type="text" name="city" class="form-control"
                           placeholder="City" value="<%= fCity %>" required>
                </div>
                <div class="col-md-2">
                    <select name="accountType" class="form-select" required>
                        <option <%= "Current".equals(fType) ? "selected" : "" %>>Current</option>
                        <option <%= "Savings".equals(fType) ? "selected" : "" %>>Savings</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <input type="number" step="0.01" name="balance" class="form-control"
                           placeholder="Balance" value="<%= fBal %>" required>
                </div>
                <div class="col-md-4">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <a href="simple_customer_jsp.jsp" class="btn btn-secondary">Reset</a>
                </div>
            </form>
        </div>
    </div>

    <%-- ---- Listing all rows ---- --%>
    <table class="table table-bordered table-striped">
        <thead class="table-dark">
            <tr><th>Account</th><th>Name</th><th>Gender</th><th>DOB</th>
                <th>State</th><th>City</th><th>Type</th><th>Balance</th><th>Actions</th></tr>
        </thead>
        <tbody>
        <%
            Statement listSt = con.createStatement();
            ResultSet listRs = listSt.executeQuery(
                "SELECT * FROM simple_customer_jsp ORDER BY account_number");
            int count = 0;
            while (listRs.next()) {
                count++;
        %>
            <tr>
                <td><%= listRs.getString("account_number") %></td>
                <td><%= listRs.getString("holder_name") %></td>
                <td><%= listRs.getString("gender") %></td>
                <td><%= listRs.getString("dob") %></td>
                <td><%= listRs.getString("state") %></td>
                <td><%= listRs.getString("city") %></td>
                <td><%= listRs.getString("account_type") %></td>
                <td><%= listRs.getDouble("balance") %></td>
                <td>
                    <a class="btn btn-sm btn-warning"
                       href="simple_customer_jsp.jsp?action=edit&accountNumber=<%= listRs.getString("account_number") %>">Edit</a>
                    <a class="btn btn-sm btn-danger"
                       href="simple_customer_jsp.jsp?action=delete&accountNumber=<%= listRs.getString("account_number") %>"
                       onclick="return confirm('Delete this row?');">Delete</a>
                </td>
            </tr>
        <%
            }
            listRs.close();
            listSt.close();
            if (count == 0) {
        %>
            <tr><td colspan="9" class="text-center">No rows yet.</td></tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
<%
    } catch (SQLException ex) {
%>
<div class="alert alert-danger">DB error: <%= ex.getMessage() %></div>
<%
    } finally {
        DBConnection.close(con);
    }
%>
