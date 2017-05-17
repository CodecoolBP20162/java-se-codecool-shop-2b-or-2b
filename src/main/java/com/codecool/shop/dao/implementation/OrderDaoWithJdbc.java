package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.CustomerDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Customer;
import com.codecool.shop.model.Order;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.05.10..
 */
public class OrderDaoWithJdbc implements OrderDao {

    private static OrderDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected OrderDaoWithJdbc() {
    }

    public static OrderDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new OrderDaoWithJdbc();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        Customer customer = order.getCustomer();
        CustomerDao customerDataStore = CustomerDaoWithJdbc.getInstance();
        int customerId = customerDataStore.findByPhoneNumber(customer.getPhoneNumber());

        String query = "INSERT INTO orders (customer_id, total_price)" +
                "VALUES ('" + customerId + "','"
                + order.calculateTotalPrice() + "');";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    @Override
    public void clearAll() {
        String query = "DELETE FROM orders;";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Order> getOrdersByCustomerId(int customer_id) {
        String query = "SELECT id FROM orders WHERE customer_id=" + customer_id + ";";
        return queryExecuteHandler(query);
    }

    @Override
    public List<Order> getShoppingCartContent() {
        return null;
    }

    public Order find(int id) {
        String query = "SELECT * FROM orders LEFT JOIN customers ON orders.customer_id=customer.id WHERE orders.customer_id ='" + id + "';";
        Order order = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {

                Customer customer = new Customer(
                        result.getString("name"),
                        result.getString("email"),
                        Integer.valueOf(result.getString("phone_number")),
                        result.getString("billing_address"),
                        result.getString("shipping_address"));
                order = new Order(customer);
                order.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }

    private ArrayList<Order> queryExecuteHandler(String query) {
        ArrayList<Order> allOrders = new ArrayList<>();
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Order order = find(result.getInt("id"));
                order.setId(result.getInt("id"));
                allOrders.add(order);
            }
            return allOrders;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
