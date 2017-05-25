package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;

import java.sql.*;


/**
 * Created by joker on 2017.05.10..
 * <h1>CustomerDaoWithJdbc class!</h1>
 * The CustomerDaoWithJdbc class implements the CustomerDao interface.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * add new customer to database,
 * find a customer by an id
 * find a customer by phone number.
 */
public class CustomerDaoWithJdbc implements CustomerDao {

    private static CustomerDaoWithJdbc instance = null;

    /**
     * Default constructor.
     */
    protected CustomerDaoWithJdbc() {
    }

    /**
     * This constructor prevents any other class from instantiating.
     * @return CustomerDaoWithJdbc instance (singleton).
     */
    public static CustomerDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new CustomerDaoWithJdbc();
        }
        return instance;
    }

    /**
     * The add method saves the customer data (name, email, phone number, billing and shipping address) to database.
     * Catch SQLException if DB connection is failed.
     * PreparedStatement class is help to prevent SQL injection.
     *
     * @param customer The customer instance to save data to database.
    */
    @Override
    public void add(Customer customer) {

        System.out.println(customer.getName());
        System.out.println(customer.getEmail());
        try (Connection connection = DBController.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customers (name, email, " +
                    "phone_number, billing_address, shipping_address) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, String.valueOf(customer.getName()));
            pstmt.setString(2, String.valueOf(customer.getEmail()));
            pstmt.setInt(3, Integer.valueOf(customer.getPhoneNumber()));
            pstmt.setString(4, String.valueOf(customer.getBillingAddress()));
            pstmt.setString(5, String.valueOf(customer.getShippingAddress()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The find method is searching in customers table and returns the customer with the given id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of customer to find in database.
     * @return customer instance.
     */
    @Override
    public Customer find(int id) {
        //Returns the customer with the given id in the db
        String query = "SELECT * FROM customers WHERE id='" + id + "';";
        Customer customer = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                customer = new Customer(
                        result.getString("name"),
                        result.getString("email"),
                        result.getInt("phone_number"),
                        result.getString("billing_address"),
                        result.getString("shipping_address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customer;
    }


    /**
     * The findByPhoneNumber method is searching in customers table and returns the customer's id with the given phoneNumber.
     * Catch SQLException if DB connection or query fails.
     *
     * @param phoneNumber The phone number of the customer to find in database.
     * @return id of the Customer.
     */
    @Override
    public int findByPhoneNumber(int phoneNumber) {
        //Returns the customer with the given id in the db
        String query = "SELECT * FROM customers WHERE phone_number='" + phoneNumber + "';";
        int id = 0;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                id = result.getInt("id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;

    }

}
