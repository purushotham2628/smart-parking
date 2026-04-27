<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.*" %>
<%
    List<ParkingArea> areas    = (List<ParkingArea>) request.getAttribute("areas");
    List<Pricing>     pricings = (List<Pricing>)     request.getAttribute("pricings");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Pricing</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div>
        <a href="<%=request.getContextPath()%>/admin/AdminDashboardServlet">Dashboard</a>
        <a href="<%=request.getContextPath()%>/admin/AdminAreaServlet">Areas</a>
        <a href="<%=request.getContextPath()%>/admin/AdminSlotServlet">Slots</a>
        <a href="<%=request.getContextPath()%>/admin/AdminBookingsServlet">Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Set / Update Pricing</h2>
        <form method="post" action="<%=request.getContextPath()%>/admin/AdminPricingServlet">
            <div class="row">
                <div style="flex:2;">
                    <label>Area</label>
                    <select name="areaId" required>
                        <% for (ParkingArea a : areas) { %>
                            <option value="<%=a.getAreaId()%>"><%=a.getName()%></option>
                        <% } %>
                    </select>
                </div>
                <div style="flex:1;">
                    <label>Vehicle</label>
                    <select name="vehicleType"><option>2W</option><option>4W</option></select>
                </div>
                <div style="flex:1;">
                    <label>Price/hr (₹)</label>
                    <input type="number" step="0.01" name="pricePerHour" required>
                </div>
                <div style="flex:1;">
                    <label>Peak Multiplier</label>
                    <input type="number" step="0.01" name="peakMultiplier" value="1.50" required>
                </div>
                <div style="flex:1;">
                    <label>Peak Start</label>
                    <input type="time" name="peakStart" value="18:00" required>
                </div>
                <div style="flex:1;">
                    <label>Peak End</label>
                    <input type="time" name="peakEnd" value="21:00" required>
                </div>
            </div>
            <button>Save Pricing</button>
        </form>
    </div>

    <div class="card">
        <h2>Existing Pricing</h2>
        <table>
            <tr><th>Area ID</th><th>Vehicle</th><th>Price/hr</th><th>Peak ×</th><th>Peak Start</th><th>Peak End</th></tr>
            <% for (Pricing p : pricings) { %>
            <tr>
                <td><%=p.getAreaId()%></td>
                <td><%=p.getVehicleType()%></td>
                <td>₹<%=p.getPricePerHour()%></td>
                <td><%=p.getPeakMultiplier()%></td>
                <td><%=p.getPeakStart()%></td>
                <td><%=p.getPeakEnd()%></td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
<script src="<%=request.getContextPath()%>/js/theme.js"></script>
</body>
</html>
