<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.parking.model.User" %>
<% User u = (User) session.getAttribute("user"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Smart Parking</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking</h1>
    <div>
        Welcome, <%= u.getName() %>
        <a href="<%=request.getContextPath()%>/user/SlotServlet">Book Slot</a>
        <a href="<%=request.getContextPath()%>/user/HistoryServlet">My Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Hello, <%= u.getName() %></h2>
        <p class="muted">Reserve a parking slot in three quick steps: pick area, time, and slot.</p>
        <div style="margin-top:18px;">
            <a class="btn" href="<%=request.getContextPath()%>/user/SlotServlet">Find a Slot</a>
            <a class="btn btn-secondary" href="<%=request.getContextPath()%>/user/HistoryServlet">View History</a>
        </div>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/theme.js"></script>
</body>
</html>
