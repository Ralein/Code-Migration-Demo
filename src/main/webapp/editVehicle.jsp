<%@ page import="com.automotive.Vehicle" %>

<%
Vehicle vehicle =
(Vehicle)request.getAttribute("vehicle");
%>

<!DOCTYPE html>
<html>

<head>

<title>Edit Vehicle</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="navbar">
<h1>Vehicle Service Management System</h1>
</div>

<div class="container">

<div class="card">

<h2>Edit Vehicle</h2>

<form action="updateVehicle"
method="post">

<input type="hidden"
name="vehicleId"
value="<%=vehicle.getVehicleId()%>">

<label>Owner Name</label>

<input type="text"
name="ownerName"
value="<%=vehicle.getOwnerName()%>"
required>

<label>Model Name</label>

<input type="text"
name="modelName"
value="<%=vehicle.getModelName()%>"
required>

<label>Registration Number</label>

<input type="text"
name="regNumber"
value="<%=vehicle.getRegNumber()%>"
required>

<button type="submit">
Update Vehicle
</button>

</form>

</div>

</div>

</body>

</html>