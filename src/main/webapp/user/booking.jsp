<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.parking.model.*" %>
<%
    Booking b = (Booking) request.getAttribute("booking");
    ParkingSlot s = (ParkingSlot) request.getAttribute("slot");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Booking Confirmed</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking</h1>
    <div>
        <a href="<%=request.getContextPath()%>/user/home.jsp">Home</a>
        <a href="<%=request.getContextPath()%>/user/HistoryServlet">My Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container" style="max-width:520px;">
    <div class="card center">
        <div class="alert success">Booking Confirmed</div>
        <h2>Booking ID: <%= b.getBookingId() %></h2>
        <table style="margin-top:16px;">
            <tr><th>Slot</th><td><%= s.getSlotNumber() %> (<%= s.getVehicleType() %>)</td></tr>
            <tr><th>Start</th><td><%= b.getStartTime() %></td></tr>
            <tr><th>End</th><td><%= b.getEndTime() %></td></tr>
            <tr><th>Duration</th><td><%= b.getDurationHours() %> hr(s)</td></tr>
            <tr><th>Total</th><td>₹ <%= b.getTotalPrice() %></td></tr>
            <tr><th>Status</th><td><%= b.getStatus() %></td></tr>
        </table>
        <div style="margin-top:18px;">
            <a class="btn" href="<%=request.getContextPath()%>/user/HistoryServlet">View History</a>
            <a class="btn btn-secondary" href="<%=request.getContextPath()%>/user/SlotServlet">Book Another</a>
        </div>
    </div>
</div>
</body>
</html>
