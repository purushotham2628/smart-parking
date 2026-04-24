<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html><head>
<title>Error</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.css">
</head><body>
<div class="container">
    <div class="card">
        <h2>Something went wrong</h2>
        <p class="muted"><%= exception != null ? exception.getMessage() : "An unexpected error occurred." %></p>
        <p style="margin-top:14px;"><a href="<%=request.getContextPath()%>/index.jsp" class="btn">Back to Home</a></p>
    </div>
</div>
</body></html>
