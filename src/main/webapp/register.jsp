<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register - Smart Parking</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking</h1>
    <div><a href="index.jsp">Home</a><a href="login.jsp">Login</a></div>
</div>

<div class="container" style="max-width:480px;">
    <div class="card">
        <h2 class="center" style="margin-bottom:16px;">Create Account</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form method="post" action="RegisterServlet">
            <label>Full Name</label><input type="text" name="name" required>
            <div style="height:8px;"></div>
            <label>Email</label><input type="email" name="email" required>
            <div style="height:8px;"></div>
            <label>Phone</label><input type="text" name="phone" pattern="[0-9]{10}" placeholder="10 digits">
            <div style="height:8px;"></div>
            <label>Password (min 4 chars)</label><input type="password" name="password" minlength="4" required>
            <div style="height:16px;"></div>
            <button style="width:100%;">Register</button>
        </form>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/theme.js"></script>
</body>
</html>
