<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.parking.model.User, com.parking.dao.BookingDAO" %>
<%
    User u = (User) session.getAttribute("user");
    // If accessed directly, populate stats lazily
    if (request.getAttribute("totalBookings") == null) {
        BookingDAO d = new BookingDAO();
        request.setAttribute("totalBookings", d.countAll());
        request.setAttribute("totalRevenue", d.totalRevenue());
        request.setAttribute("totalAreas", new com.parking.dao.ParkingAreaDAO().getAll().size());
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div>
        <%= u.getName() %>
        <a href="<%=request.getContextPath()%>/admin/AdminAreaServlet">Areas</a>
        <a href="<%=request.getContextPath()%>/admin/AdminSlotServlet">Slots</a>
        <a href="<%=request.getContextPath()%>/admin/AdminPricingServlet">Pricing</a>
        <a href="<%=request.getContextPath()%>/admin/AdminBookingsServlet">Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Dashboard</h2>
        <div class="stats" style="margin-top:18px;">
            <div class="stat"><h3>Total Bookings</h3><div class="num"><%=request.getAttribute("totalBookings")%></div></div>
            <div class="stat"><h3>Total Revenue</h3><div class="num">₹ <%=request.getAttribute("totalRevenue")%></div></div>
            <div class="stat"><h3>Parking Areas</h3><div class="num"><%=request.getAttribute("totalAreas")%></div></div>
        </div>
    </div>
</div>
</body>
</html>
