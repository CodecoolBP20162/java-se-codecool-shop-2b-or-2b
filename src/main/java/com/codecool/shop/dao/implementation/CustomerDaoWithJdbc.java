package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by joker on 2017.05.10..
 */
public class CustomerDaoWithJdbc implements CustomerDao {

    private static CustomerDaoWithJdbc instance = null;

    protected CustomerDaoWithJdbc() {
    }

    public static CustomerDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new CustomerDaoWithJdbc();
        }
        return instance;
    }


    @Override
    public void add(Customer customer) {
        int index = 1;
        System.out.println(customer.getName());
        System.out.println(customer.getEmail());
        try (Connection connection = DBController.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customers (id, name, email, " +
                    "phone_number, billing_address, shipping_address) VALUES (?, ?, ?, ?, ?, ?)");
            pstmt.setInt(1, Integer.valueOf(index));
            pstmt.setString(2, String.valueOf(customer.getName()));
            pstmt.setString(3, String.valueOf(customer.getEmail()));
            pstmt.setInt(4, Integer.valueOf(customer.getPhoneNumber()));
            pstmt.setString(5, String.valueOf(customer.getBillingAddress()));
            pstmt.setString(6, String.valueOf(customer.getShippingAddress()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
    @Override
    public Customer find(int id) {
        //Returns the customer with the given id in the db
        String query = "SELECT * FROM customers WHERE id='" + id + "';";
        Customer customer = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                customer = new Customer(result.getString("name"), result.getString("description"));
                customer.setId(id);
                customer.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(customer));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }
    */
}
