package com.codecool.shop.dao;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.OrderedItems;

import java.util.List;

/**
 * Created by joker on 2017.05.10.
 * <h1>OrderedItemsDao interface</h1>
 * OrderedItemsDaoWithJdbc class implements this interface.
 * You can do database action:
 * add new orderedItem to database,
 * get order by order Id.
 */
public interface OrderedItemsDao {

    /**
     * The add method saves the orderedItem data (order ID, product ID, quantity) to database.
     * Catch SQLException if DB connection is failed.
     *
     * @param id The ID of the order
     * @param orderedItem The orderedItem instance to save data to database.
     */
    void add(int id, LineItem orderedItem);

    /**
     * The clearAll method deletes all ordered items from database.
     * Used for tests.
     * Catch SQLException if DB connection is failed.
     */
    void clearAll();

    /**
     * This getByOrderId method returns order(s) found in database by ID
     * @param id The id of the order.
     * @return List of orders or null
     */
    List<OrderedItems> getByOrderId(int id);
}
