package com.codecool.shop.controller;

import com.codecool.shop.dao.implementation.DbConnectionProvider;

import java.sql.*;

/**
 * Created by eb on 2017.05.08..
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

    public static DbConnectionProvider getInstance() {
        return () -> {
            try {
                return getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        };
    }
}
