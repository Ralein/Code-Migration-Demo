package com.automotive;

public class ConnectionTest {

    public static void main(String[] args) {

        if(DBConnection.getConnection() != null) {

            System.out.println("Database Connected");

        } else {

            System.out.println("Database Connection Failed");
        }
    }
}