<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Search Vehicle</title>

<link rel="stylesheet"
href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="navbar">
    <h1>Vehicle Service Management System</h1>
</div>

<div class="container">

    <div class="card">

        <h2>Search Vehicle</h2>

        <form action="searchVehicle" method="get">

            <label>Vehicle ID</label>

            <input type="number"
                   name="vehicleId"
                   required>

            <button type="submit">
                Search
            </button>

        </form>

        <br>

        <a href="index.jsp">
            <button type="button">
                Back to Dashboard
            </button>
        </a>

    </div>

</div>

</body>

</html>