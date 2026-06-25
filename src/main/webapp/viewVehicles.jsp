<%@ page import="java.util.List" %>
<%@ page import="com.automotive.Vehicle" %>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>View Vehicles</title>

<link rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css">

</head>

<body>

<div class="navbar">
    <h1>Vehicle Service Management System</h1>
</div>

<div class="container">

    <div class="card">

        <h2>Vehicle List</h2>

        <%
        String message = request.getParameter("message");

        if(message != null){

            if(message.equals("success")){
        %>

        <div class="success-message">
            Vehicle Saved Successfully
        </div>

        <%
            }
            else if(message.equals("deleted")){
        %>

        <div class="success-message">
            Vehicle Deleted Successfully
        </div>

        <%
            }
            else if(message.equals("error")){
        %>

        <div class="error-message">
            Unable To Delete Vehicle
        </div>

        <%
            }
        }
        %>

        <table>

            <tr>
                <th>ID</th>
                <th>Owner</th>
                <th>Model</th>
                <th>Registration</th>
                <th>Actions</th>
            </tr>

            <%
            List<Vehicle> vehicles =
                    (List<Vehicle>)request.getAttribute("vehicles");

            if(vehicles != null){

                for(Vehicle v : vehicles){
            %>

            <tr>

                <td><%=v.getVehicleId()%></td>
                <td><%=v.getOwnerName()%></td>
                <td><%=v.getModelName()%></td>
                <td><%=v.getRegNumber()%></td>

                <td>

<a href="editVehicle?id=<%=v.getVehicleId()%>">
    <button type="button">
        Edit
    </button>
</a>

<a href="deleteVehicle?id=<%=v.getVehicleId()%>"
onclick="return confirm('Delete this vehicle?');">

    <button type="button">
        Delete
    </button>

</a>

</td>

            </tr>

            <%
                }
            }
            %>

        </table>

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