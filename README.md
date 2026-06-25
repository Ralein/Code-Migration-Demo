# VehicleServiceApp
JSP+Java8+Oracle
# Vehicle Service Management System

## Project Overview

Vehicle Service Management System is a legacy automotive web application developed using Java 8, JSP, Servlets, JDBC, Oracle Database, and Apache Tomcat.

The application allows users to:

* Add Vehicle
* View Vehicles
* Search Vehicle
* Update Vehicle
* Delete Vehicle

This project demonstrates a traditional enterprise Java application architecture commonly used in legacy automotive and manufacturing systems.

---

# Technology Stack

## Frontend

* HTML
* CSS
* JSP (Java Server Pages)

## Backend

* Java 8
* Servlets
* JDBC

## Database

* Oracle Database 21c

## Server

* Apache Tomcat 9

## IDE

* Eclipse IDE for Enterprise Java Developers

---

# Software Requirements

| Software           | Version    |
| ------------------ | ---------- |
| Java JDK           | 1.8        |
| Eclipse IDE        | 2024+      |
| Oracle Database    | 21c        |
| Apache Tomcat      | 9.x        |
| Oracle JDBC Driver | ojdbc8.jar |

---

# Project Features

## Vehicle Management

### Add Vehicle

Users can register a new vehicle by entering:

* Vehicle ID
* Owner Name
* Model Name
* Registration Number

### View Vehicles

Displays all vehicles stored in the database.

### Search Vehicle

Allows searching vehicles using Vehicle ID.

### Update Vehicle

Allows modification of:

* Owner Name
* Model Name
* Registration Number

### Delete Vehicle

Allows removal of vehicle records from the system.

---

# Project Structure

src/main/java

com.automotive

* AddVehicleServlet.java
* ViewVehiclesServlet.java
* SearchVehicleServlet.java
* DeleteVehicleServlet.java
* EditVehicleServlet.java
* UpdateVehicleServlet.java
* Vehicle.java
* VehicleDAO.java
* DBConnection.java

src/main/webapp

* index.jsp
* addVehicle.jsp
* viewVehicles.jsp
* searchVehicle.jsp
* searchResult.jsp
* editVehicle.jsp

css

* style.css

WEB-INF

---

# Database Setup

## Step 1

Open SQL Plus:

sqlplus system

Login using:

Username: system

Password: your_password

---

## Step 2

Create Vehicle Table

```sql
CREATE TABLE VEHICLE
(
    VEHICLE_ID NUMBER PRIMARY KEY,
    OWNER_NAME VARCHAR2(100),
    MODEL_NAME VARCHAR2(100),
    REG_NUMBER VARCHAR2(20)
);
```

Verify:

```sql
SELECT * FROM VEHICLE;
```

---

# Oracle JDBC Driver Setup

Download:

ojdbc8.jar

Copy the file into:

WEB-INF/lib

Example:

WebContent
└── WEB-INF
└── lib
└── ojdbc8.jar

---

# Tomcat Configuration

## Step 1

Download Apache Tomcat 9

## Step 2

Extract Tomcat

Example:

C:\Tomcat9

## Step 3

In Eclipse:

Window
→ Preferences
→ Server
→ Runtime Environments

Add:

Apache Tomcat v9.0

Select Tomcat installation folder.

---

# Database Configuration

Open:

DBConnection.java

Example:

```java
package com.automotive;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName(
                "oracle.jdbc.driver.OracleDriver");

            con =
                DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521/XEPDB1",
                "SYSTEM",
                "password");

            System.out.println(
                "Connected Successfully");

        } catch(Exception e) {

            e.printStackTrace();
        }

        return con;
    }
}
```

Replace:

password

with your Oracle SYSTEM password.

---

# Running the Application

## Step 1

Start Oracle Database.

## Step 2

Start Apache Tomcat Server.

## Step 3

Right Click Project

Run As
→ Run on Server

Select:

Apache Tomcat v9.0

Finish.

---

# Application URL

Home Page

http://localhost:8080/VehicleServiceApp/

View Vehicles

http://localhost:8080/VehicleServiceApp/viewVehicles

Search Vehicle

http://localhost:8080/VehicleServiceApp/searchVehicle.jsp

---

# Validation Implemented

## Vehicle ID

* Must be numeric
* Must be greater than zero

## Owner Name

* Minimum 3 characters

## Registration Number

Format:

TS09AB1234

Pattern:

[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}

## Mandatory Fields

All fields are required.

---

# Future Enhancements

* Dashboard Statistics
* Vehicle Service History
* Customer Management
* Insurance Management
* Service Booking
* Reporting Module
* User Authentication
* Role-Based Access Control

---

<img width="949" height="435" alt="Screenshot 2026-06-23 092940" src="https://github.com/user-attachments/assets/cff49c92-84b2-48c1-9323-e6b8f0409474" />

<img width="943" height="419" alt="Screenshot 2026-06-23 093214" src="https://github.com/user-attachments/assets/66d0e904-6a5b-4d1d-8850-2ab3a935183f" />

<img width="914" height="453" alt="Screenshot 2026-06-23 093254" src="https://github.com/user-attachments/assets/e7523688-ee57-4151-80b5-8848d6ad9d5e" />

<img width="955" height="326" alt="image" src="https://github.com/user-attachments/assets/ee9db8dc-ae41-438a-93f6-25ffa2c27d55" />


Legacy Automotive Vehicle Service Management System

Java 8 | JSP | Servlets | Oracle | JDBC | Tomcat 9
# Code-Migration-Demo
