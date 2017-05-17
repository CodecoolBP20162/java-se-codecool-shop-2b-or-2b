package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;


/**
 * Created by joker on 2017.05.10..
 */
public class CustomerDaoWithJdbc implements CustomerDao {

    private static final Logger logger = LoggerFactory.getLogger(CustomerDaoWithJdbc.class);
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
        logger.info("Add new customer to customers table...");
        try (Connection connection = DBController.getConnection()) {
            PreparedStatement pstmt = connection.prepareStatement("INSERT INTO customers (name, email, " +
                    "phone_number, billing_address, shipping_address) VALUES (?, ?, ?, ?, ?)");
            pstmt.setString(1, String.valueOf(customer.getName()));
            pstmt.setString(2, String.valueOf(customer.getEmail()));
            pstmt.setInt(3, Integer.valueOf(customer.getPhoneNumber()));
            pstmt.setString(4, String.valueOf(customer.getBillingAddress()));
            pstmt.setString(5, String.valueOf(customer.getShippingAddress()));
            pstmt.executeUpdate();
            logger.info("Customer {} is successfully added!", customer.getName());
        } catch (SQLException e) {
            logger.error("SQL exception: {}", e);
            e.printStackTrace();
        }
    }


    @Override
    public Customer find(int id) {
        //Returns the customer with the given id in the db
        logger.info("Find customer with id:{} ...", id);
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
            logger.error("SQL exception: {}", e);
            e.printStackTrace();
        }
        logger.info("Customer {} found!", customer.getName());
        return customer;
    }

    @Override
    public int findByPhoneNumber(int phoneNumber) {
        //Returns the customer with the given id in the db
        logger.info("Find customer with phoneNumber:{} ...", phoneNumber);
        String query = "SELECT * FROM customers WHERE phone_number='" + phoneNumber + "';";
        int id = 0;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                id = result.getInt("id");
            }

        } catch (SQLException e) {
            logger.error("SQL exception: {}", e);
            e.printStackTrace();
        }
        logger.info("The id of the customer is: {}", id);
        return id;

    }

}
