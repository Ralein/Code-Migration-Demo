package com.automotive;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:oracle:thin:@localhost:1521/XEPDB1";

    private static final String USER =
            "system";

    private static final String PASSWORD =
            "sql";

    public static Connection getConnection() {

        Connection con = null;

        try {

            Class.forName("oracle.jdbc.OracleDriver");

            con = DriverManager.getConnection(
                    URL,
                    USER,
                    PASSWORD);

            System.out.println("Connected Successfully");

        } catch (Exception e) {

            System.out.println("Database Connection Failed");

            e.printStackTrace();
        }

        return con;
    }
}