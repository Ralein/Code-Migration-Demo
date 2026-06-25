<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>Add Vehicle</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="navbar">
    <h1>Vehicle Service Management System</h1>
</div>

<div class="container">

    <div class="card">

        <h2>Add Vehicle</h2>
        <%
        String message = request.getParameter("message");

        if(message != null && message.equals("error")){
%>

<div class="error-message">
    Error Saving Vehicle
</div>

<%
}
%>

        <form action="addVehicle" method="post">

            <label>Vehicle ID</label>

            <input type="number"
                   name="vehicleId"
                   min="1"
                   required>

            <label>Owner Name</label>

            <input type="text"
                   name="ownerName"
                   pattern="[A-Za-z ]{3,50}"
                   title="Only alphabets allowed"
                   required>

            <label>Model Name</label>

            <input type="text"
                   name="modelName"
                   minlength="2"
                   maxlength="50"
                   required>

            <label>Registration Number</label>

            <input type="text"
                   name="regNumber"
                   pattern="[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}"
                   placeholder="TS09AB1234"
                   title="Format: TS09AB1234"
                   required>

            <button type="submit">
                Save Vehicle
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