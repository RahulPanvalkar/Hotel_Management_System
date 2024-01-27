package com.utility;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionProvider {
    private static Connection con;

    public static Connection getConnection() {
        try {

            if (con == null) {

                // Load Driver Class
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish the database connection
                String url = "jdbc:mysql://localhost:3306/HotelManagement_Db";

                con = DriverManager.getConnection(url, "root", "root");
                // Debug statements
                System.out.println("Database connection successful!");
                System.out.println("Connected to: " + url);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}