<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.*" %>
<%
    List<ParkingArea> areas = (List<ParkingArea>) request.getAttribute("areas");
    List<ParkingSlot> slots = (List<ParkingSlot>) request.getAttribute("slots");
    Integer selAreaId = (Integer) request.getAttribute("selectedAreaId");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Slots</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div>
        <a href="<%=request.getContextPath()%>/admin/AdminDashboardServlet">Dashboard</a>
        <a href="<%=request.getContextPath()%>/admin/AdminAreaServlet">Areas</a>
        <a href="<%=request.getContextPath()%>/admin/AdminPricingServlet">Pricing</a>
        <a href="<%=request.getContextPath()%>/admin/AdminBookingsServlet">Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Manage Slots</h2>
        <form method="get" action="<%=request.getContextPath()%>/admin/AdminSlotServlet">
            <label>Select Area</label>
            <select name="areaId" onchange="this.form.submit()">
                <option value="">-- Choose --</option>
                <% for (ParkingArea a : areas) { %>
                    <option value="<%=a.getAreaId()%>"
                        <%= selAreaId != null && selAreaId == a.getAreaId() ? "selected" : "" %>>
                        <%=a.getName()%>
                    </option>
                <% } %>
            </select>
        </form>
    </div>

    <% if (selAreaId != null) { %>
    <div class="card">
        <h3>Add Slot</h3>
        <form method="post" action="<%=request.getContextPath()%>/admin/AdminSlotServlet">
            <input type="hidden" name="action" value="add">
            <input type="hidden" name="areaId" value="<%=selAreaId%>">
            <div class="row">
                <div style="flex:1;"><label>Slot Number</label><input name="slotNumber" required></div>
                <div style="flex:1;"><label>Vehicle Type</label>
                    <select name="vehicleType"><option>2W</option><option>4W</option></select>
                </div>
            </div>
            <button>Add Slot</button>
        </form>
    </div>

    <div class="card">
        <h3>Slots in this area</h3>
        <table>
            <tr><th>ID</th><th>Slot No</th><th>Type</th><th>Active</th><th></th></tr>
            <% if (slots != null) for (ParkingSlot s : slots) { %>
            <tr>
                <td><%=s.getSlotId()%></td>
                <td><%=s.getSlotNumber()%></td>
                <td><%=s.getVehicleType()%></td>
                <td><%=s.isActive()?"Yes":"No"%></td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/admin/AdminSlotServlet"
                          onsubmit="return confirm('Delete slot?');" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="slotId" value="<%=s.getSlotId()%>">
                        <input type="hidden" name="areaId" value="<%=selAreaId%>">
                        <button class="btn btn-danger" style="padding:5px 10px;">Delete</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
    <% } %>
</div>
</body>
</html>
