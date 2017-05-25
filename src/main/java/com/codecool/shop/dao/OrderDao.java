package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.04.27.
 * <h1>OrderDao interface</h1>
 * OrderDaoWithJdbc class implements this interface.
 * OrderDaoMem class implements this interface.
 * You can do database action:
 * add new order to database,
 * get orders by customer Id,
 * get shopping cart content.
 */
public interface OrderDao {

    /**
     * The add method can save the order data to database.
     * @param order The order instance to save data to database.
     */
    void add(Order order);

    /**
     * The clearAll method can delete all orders from database.
     * Used for tests.
     */
    void clearAll();
    ArrayList<Order> getOrdersByCustomerId(int customer_id);


    /**
     * This method can get shopping cart content.
     * @return List of order / or null
     */
    List<Order> getShoppingCartContent();

}
