package com.codecool.shop.controller;

import java.sql.*;

/**
 * Created by eb on 2017.05.08..
 * This class creates the Database connection
 */

public class DBController {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

}
