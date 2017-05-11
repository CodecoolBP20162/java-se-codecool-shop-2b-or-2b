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
 */
public class OrderedItemsDaoWithJdbc implements OrderedItemsDao {

    private static OrderedItemsDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected OrderedItemsDaoWithJdbc() {
    }

    public static OrderedItemsDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new OrderedItemsDaoWithJdbc();
        }
        return instance;
    }

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



    @Override
    public void clearAll() {

    }

    @Override
    public List<OrderedItems> getByOrderId(int id) {
        return null;
    }

}
