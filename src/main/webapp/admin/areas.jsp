<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, com.parking.model.ParkingArea" %>
<% List<ParkingArea> areas = (List<ParkingArea>) request.getAttribute("areas"); %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Manage Areas</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head>
<body>
<div class="navbar">
    <h1>Smart Parking — Admin</h1>
    <div>
        <a href="<%=request.getContextPath()%>/admin/AdminDashboardServlet">Dashboard</a>
        <a href="<%=request.getContextPath()%>/admin/AdminSlotServlet">Slots</a>
        <a href="<%=request.getContextPath()%>/admin/AdminPricingServlet">Pricing</a>
        <a href="<%=request.getContextPath()%>/admin/AdminBookingsServlet">Bookings</a>
        <a href="<%=request.getContextPath()%>/LogoutServlet">Logout</a>
    </div>
</div>

<div class="container">
    <div class="card">
        <h2>Add Parking Area</h2>
        <form method="post" action="<%=request.getContextPath()%>/admin/AdminAreaServlet">
            <input type="hidden" name="action" value="add">
            <div class="row">
                <div style="flex:1;"><label>Name</label><input name="name" required></div>
                <div style="flex:1;"><label>Location</label><input name="location" required></div>
            </div>
            <label>Description</label><textarea name="description" rows="2"></textarea>
            <div style="height:10px;"></div>
            <button>Add Area</button>
        </form>
    </div>

    <div class="card">
        <h2>Existing Areas</h2>
        <table>
            <tr><th>ID</th><th>Name</th><th>Location</th><th>Description</th><th></th></tr>
            <% for (ParkingArea a : areas) { %>
            <tr>
                <td><%=a.getAreaId()%></td>
                <td><%=a.getName()%></td>
                <td><%=a.getLocation()%></td>
                <td><%=a.getDescription()%></td>
                <td>
                    <form method="post" action="<%=request.getContextPath()%>/admin/AdminAreaServlet"
                          onsubmit="return confirm('Delete area and all its slots?');" style="display:inline;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="areaId" value="<%=a.getAreaId()%>">
                        <button class="btn btn-danger" style="padding:5px 10px;">Delete</button>
                    </form>
                </td>
            </tr>
            <% } %>
        </table>
    </div>
</div>
</body>
</html>
