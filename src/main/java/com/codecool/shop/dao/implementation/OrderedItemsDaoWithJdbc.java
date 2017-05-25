package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.OrderedItemsDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.OrderedItems;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by joker on 2017.05.10..
 * <h1>OrderedItemsDaoWithJdbc class!</h1>
 * The OrderedItemsDaoWithJdbc class implements the OrderedItemsDao interface.
 * Singleton class, can be created only one instance.
 * You can do database action:
 * add new orderedItem to database.
 */
public class OrderedItemsDaoWithJdbc implements OrderedItemsDao {

    private static OrderedItemsDaoWithJdbc instance = null;

    /**
     * Default constructor.
     */
    protected OrderedItemsDaoWithJdbc() {
    }

    /**
     * This constructor prevents any other class from instantiating.
     * @return OrderedItemsDaoWithJdbc instance (singleton).
     */
    public static OrderedItemsDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new OrderedItemsDaoWithJdbc();
        }
        return instance;
    }

    /**
     * The add method saves the orderedItem data (order ID, product ID, quantity) to database.
     * Catch SQLException if DB connection is failed.
     *
     * @param orderedItem The orderedItem instance to save data to database.
     */
    @Override
    public void add(int id, LineItem orderedItem) {

        String query = "INSERT INTO ordered_items (order_id, product_id, quantity)"  +
                "VALUES ('" + id + "','" + orderedItem.getProduct().getId() + "','" + orderedItem.getQuantity()+"');";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * The clearAll method deletes all ordered items from database.
     * Used for tests.
     * Catch SQLException if DB connection is failed.
     */
    @Override
    public void clearAll() {

    }

    /**
     * This getByOrderId method returns order(s) found in database by ID
     * @param id The id of the order.
     * @return List of orders or null
     */
    @Override
    public List<OrderedItems> getByOrderId(int id) {
        return null;
    }

}
