package com.codecool.shop.controller;

import java.sql.*;

/**
 * Created by eb on 2017.05.08..
 */
public class DBController {

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                DATABASE,
                DB_USER,
                DB_PASSWORD);
    }

    /*
    public void add() {
        String name = "Liza";
        String age = "21";
        String query = "INSERT INTO products (name, age) " +
                "VALUES ('" + name + "', '" + age + "');";
        executeQuery(query);
        System.out.println("ok");
    }
    */

    private void executeQuery(String query) {
        try (Connection connection = getConnection();
             Statement statement =connection.createStatement();
        ){
            statement.execute(query);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
