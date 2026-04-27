<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.*" %>
<%
    List<ParkingArea> areas = (List<ParkingArea>) request.getAttribute("areas");
    List<ParkingSlot> slots = (List<ParkingSlot>) request.getAttribute("slots");
    ParkingArea selected = (ParkingArea) request.getAttribute("selectedArea");
    String date = (String) request.getAttribute("date");
    String time = (String) request.getAttribute("time");
    Integer hours = (Integer) request.getAttribute("hours");
    Pricing p4w = (Pricing) request.getAttribute("p4w");
    Pricing p2w = (Pricing) request.getAttribute("p2w");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Book Slot - Smart Parking</title>
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

<div class="container">
    <div class="card">
        <h2>Find Parking Slot</h2>
        <form method="get" action="<%=request.getContextPath()%>/user/SlotServlet">
            <div class="row">
                <div style="flex:2;">
                    <label>Parking Area</label>
                    <select name="areaId" required>
                        <option value="">-- Select Area --</option>
                        <% for (ParkingArea a : areas) { %>
                            <option value="<%=a.getAreaId()%>"
                                <%= selected != null && selected.getAreaId()==a.getAreaId() ? "selected" : "" %>>
                                <%=a.getName()%> — <%=a.getLocation()%>
                            </option>
                        <% } %>
                    </select>
                </div>
                <div style="flex:1;">
                    <label>Date</label>
                    <input type="date" name="date" value="<%= date != null ? date : "" %>" required>
                </div>
                <div style="flex:1;">
                    <label>Start Time</label>
                    <input type="time" name="time" value="<%= time != null ? time : "10:00" %>" required>
                </div>
                <div style="flex:1;">
                    <label>Duration (hrs)</label>
                    <input type="number" name="hours" min="1" max="24" value="<%= hours != null ? hours : 1 %>" required>
                </div>
            </div>
            <button type="submit">Check Availability</button>
        </form>
    </div>

    <% if (slots != null) { %>
        <div class="card">
            <h3><%= selected.getName() %> — <%= date %> @ <%= time %> for <%= hours %> hr(s)</h3>
            <p class="muted">
                Pricing:
                <% if (p4w != null) { %> 4-Wheeler ₹<%= p4w.getPricePerHour() %>/hr <% } %>
                <% if (p2w != null) { %> | 2-Wheeler ₹<%= p2w.getPricePerHour() %>/hr <% } %>
                (peak multiplier may apply)
            </p>

            <h4 style="margin-top:18px;">4-Wheeler Slots</h4>
            <div class="slot-grid">
                <% for (ParkingSlot s : slots) { if (!"4W".equals(s.getVehicleType())) continue; %>
                    <div class="slot <%= s.isBooked() ? "booked" : "available" %>"
                         data-slot-id="<%=s.getSlotId()%>"
                         data-slot-number="<%=s.getSlotNumber()%>"
                         data-vehicle-type="<%=s.getVehicleType()%>">
                        <%=s.getSlotNumber()%>
                        <small><%= s.isBooked() ? "Booked" : "Free" %></small>
                    </div>
                <% } %>
            </div>

            <h4 style="margin-top:18px;">2-Wheeler Slots</h4>
            <div class="slot-grid">
                <% for (ParkingSlot s : slots) { if (!"2W".equals(s.getVehicleType())) continue; %>
                    <div class="slot <%= s.isBooked() ? "booked" : "available" %>"
                         data-slot-id="<%=s.getSlotId()%>"
                         data-slot-number="<%=s.getSlotNumber()%>"
                         data-vehicle-type="<%=s.getVehicleType()%>">
                        <%=s.getSlotNumber()%>
                        <small><%= s.isBooked() ? "Booked" : "Free" %></small>
                    </div>
                <% } %>
            </div>

            <div class="legend">
                <span><i class="av"></i>Available</span>
                <span><i class="bk"></i>Booked</span>
                <span><i class="sl"></i>Selected</span>
            </div>

            <form method="post" action="<%=request.getContextPath()%>/user/BookingServlet" style="margin-top:20px;">
                <input type="hidden" name="slotId" id="slotIdInput">
                <input type="hidden" name="date"  value="<%=date%>">
                <input type="hidden" name="time"  value="<%=time%>">
                <input type="hidden" name="hours" value="<%=hours%>">
                <p id="selectedSlotLabel" class="muted">Click an available slot to select it.</p>
                <button type="submit" id="bookBtn" disabled style="margin-top:10px;">Confirm Booking</button>
            </form>
        </div>
    <% } %>
</div>

<script src="<%=request.getContextPath()%>/js/slots.js"></script>
<script src="<%=request.getContextPath()%>/js/theme.js"></script>
</body>
</html>
