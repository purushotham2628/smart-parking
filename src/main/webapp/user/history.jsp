<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.Booking" %>
<% List<Booking> list = (List<Booking>) request.getAttribute("bookings"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>My Bookings</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking</h1>
    <div>
        <a href="<%=request.getContextPath()%>/user/home.jsp">Home</a>
        <a href="<%=request.getContextPath()%>/user/SlotServlet">Book Slot</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>My Booking History</h2>
        <% if (list == null || list.isEmpty()) { %>
            <p class="muted" style="margin-top:10px;">No bookings yet.</p>
        <% } else { %>
        <table>
            <tr>
                <th>Booking ID</th><th>Area</th><th>Slot</th><th>Vehicle</th>
                <th>Start</th><th>End</th><th>Total</th><th>Status</th><th></th>
            </tr>
            <% for (Booking b : list) {
                boolean future = b.getStartTime().getTime() > System.currentTimeMillis();
                boolean active = "ACTIVE".equals(b.getStatus()); %>
            <tr>
                <td><%=b.getBookingId()%></td>
                <td><%=b.getAreaName()%></td>
                <td><%=b.getSlotNumber()%></td>
                <td><%=b.getVehicleType()%></td>
                <td><%=b.getStartTime()%></td>
                <td><%=b.getEndTime()%></td>
                <td>₹<%=b.getTotalPrice()%></td>
                <td><%=b.getStatus()%></td>
                <td>
                    <% if (future && active) { %>
                        <form method="post" action="<%=request.getContextPath()%>/user/CancelBookingServlet"
                              onsubmit="return confirm('Cancel this booking?');">
                            <input type="hidden" name="bookingId" value="<%=b.getBookingId()%>">
                            <button class="btn btn-danger" style="padding:5px 10px;">Cancel</button>
                        </form>
                    <% } %>
                </td>
            </tr>
            <% } %>
        </table>
        <% } %>
    </div>
</div>
</body>
</html>
