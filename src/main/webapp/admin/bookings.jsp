<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.Booking" %>
<% List<Booking> list = (List<Booking>) request.getAttribute("bookings"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>All Bookings</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div>
        <a href="<%=request.getContextPath()%>/admin/AdminDashboardServlet">Dashboard</a>
        <a href="<%=request.getContextPath()%>/admin/AdminAreaServlet">Areas</a>
        <a href="<%=request.getContextPath()%>/admin/AdminSlotServlet">Slots</a>
        <a href="<%=request.getContextPath()%>/admin/AdminPricingServlet">Pricing</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>All Bookings</h2>
        <table>
            <tr>
                <th>Booking ID</th><th>User</th><th>Area</th><th>Slot</th>
                <th>Vehicle</th><th>Start</th><th>End</th><th>Total</th><th>Status</th>
            </tr>
            <% if (list != null) for (Booking b : list) { %>
            <tr>
                <td><%=b.getBookingId()%></td>
                <td><%=b.getUserName()%></td>
                <td><%=b.getAreaName()%></td>
                <td><%=b.getSlotNumber()%></td>
                <td><%=b.getVehicleType()%></td>
                <td><%=b.getStartTime()%></td>
                <td><%=b.getEndTime()%></td>
                <td>₹<%=b.getTotalPrice()%></td>
                <td><%=b.getStatus()%></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>
