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
 * <h1>OrderDaoWithJdbc class!</h1>
 * The OrderDaoWithJdbc class implements the OrderDao interface.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * add new order to database,
 * get orders by customer Id from database,
 * get shopping cart content,
 * find a orders by customer Id in database.
 */
public class OrderDaoWithJdbc implements OrderDao{

    private static OrderDaoWithJdbc instance = null;

    /**
     * Default constructor.
     */
    protected OrderDaoWithJdbc() {
    }

    /**
     * This constructor prevents any other class from instantiating.
     * @return OrderDaoWithJdbc instance (singleton).
     */
    public static OrderDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new OrderDaoWithJdbc();
        }
        return instance;
    }

    /**
     * The add method saves the order data (customer ID, total price) to database.
     * Catch SQLException if DB connection is failed.
     *
     * @param order The order instance to save data to database.
     */
    @Override
    public void add(Order order){
        Customer customer = order.getCustomer();
        CustomerDao customerDataStore = CustomerDaoWithJdbc.getInstance();
        int customerId = customerDataStore.findByPhoneNumber(customer.getPhoneNumber());

        String query = "INSERT INTO orders (customer_id, total_price)" +
                "VALUES ('" + customerId + "','"
                + order.calculateTotalPrice()+"');";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * The clearAll method deletes all orders from database.
     * Used for tests.
     * Catch SQLException if DB connection is failed.
     */
    @Override
    public void clearAll() {
        String query = "DELETE FROM orders;";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getOrdersByCustomerId method is searching in customers table and returns the customer with the given id.
     *
     * @param customer_id The id of customer to find in database.
     * @return Order array list.
     * @see #queryExecuteHandler(String)
     */
    @Override
    public ArrayList<Order> getOrdersByCustomerId(int customer_id) {
        String query = "SELECT id FROM orders WHERE customer_id=" + customer_id + ";";
        return queryExecuteHandler(query);
    }

    /**
     * This method gets shopping cart content.
     * @return List of order / or null
     */
    @Override
    public List<Order> getShoppingCartContent() {
        return null;
    }

    /**
     * The find method is searching orders in database by given a given order ID and returns the order.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of customer to find in database.
     * @return Order instance.
     */
    public Order find(int id) {
        String query = "SELECT * FROM orders LEFT JOIN customers ON orders.customer_id=customer.id " +
                "WHERE orders.customer_id ='" + id + "';";
        Order order = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()){
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

    /**
     * This method is a query helper used by getAll() and getBy() methods.
     * Catch SQLException if DB connection or query fails.
     * @param query String type
     * @see #getOrdersByCustomerId(int)
     * @return An Array List of orders / or NULL
     */
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
