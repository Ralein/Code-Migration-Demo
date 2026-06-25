<%@ page import="com.automotive.Vehicle"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Search Result</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="navbar">
    <h1>Vehicle Service Management System</h1>
</div>

<div class="container">

<div class="card">

<h2>Search Result</h2>

<%
Vehicle vehicle =
(Vehicle)request.getAttribute("vehicle");

if(vehicle != null){
%>

<table>

<tr>
    <th>ID</th>
    <th>Owner</th>
    <th>Model</th>
    <th>Registration</th>
</tr>

<tr>

<td><%=vehicle.getVehicleId()%></td>

<td><%=vehicle.getOwnerName()%></td>

<td><%=vehicle.getModelName()%></td>

<td><%=vehicle.getRegNumber()%></td>

</tr>

</table>

<%
}
else{
%>

<div class="error-message">
Vehicle Not Found
</div>

<%
}
%>

<br>

<a href="searchVehicle.jsp">
<button>
Search Again
</button>
</a>

</div>

</div>

</body>

</html>