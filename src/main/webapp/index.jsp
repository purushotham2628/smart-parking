<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Smart Parking - Home</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking</h1>
    <div>
        <a href="login.jsp">Login</a>
        <a href="register.jsp">Register</a>
        <a href="admin/login.jsp">Admin</a>
    </div>
</div>

<div class="container">
    <div class="card center">
        <h2 style="margin-bottom:12px;">Welcome to Smart Parking Slot Booking</h2>
        <p class="muted">Find, reserve and pay for a parking slot in seconds.</p>
        <div style="margin-top:24px;">
            <a href="login.jsp" class="btn">Get Started</a>
        </div>
    </div>

    <div class="stats">
        <div class="stat"><h3>Easy Booking</h3><div class="num">3 Steps</div></div>
        <div class="stat"><h3>Live Availability</h3><div class="num">Real-time</div></div>
        <div class="stat"><h3>Secure Payment</h3><div class="num">100%</div></div>
    </div>
</div>
</body>
</html>
