<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Login</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div><a href="<%=request.getContextPath()%>/index.jsp">Home</a></div>
</div>

<div class="container" style="max-width:420px;">
    <div class="card">
        <h2 class="center" style="margin-bottom:16px;">Admin Login</h2>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert error"><%=request.getAttribute("error")%></div>
        <% } %>
        <form method="post" action="<%=request.getContextPath()%>/LoginServlet">
            <label>Email</label><input type="email" name="email" required>
            <div style="height:10px;"></div>
            <label>Password</label><input type="password" name="password" required>
            <div style="height:16px;"></div>
            <button style="width:100%;">Login</button>
        </form>
        <p class="muted center" style="margin-top:14px;">Demo: admin@parking.com / admin123</p>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/theme.js"></script>
</body>
</html>
