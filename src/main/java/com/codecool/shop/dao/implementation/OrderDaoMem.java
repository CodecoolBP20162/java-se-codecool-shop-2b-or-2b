package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by joker on 2017.04.27..
 */
public class OrderDaoMem implements OrderDao {

    private List<Order> DATA = new ArrayList<>();
    private static OrderDaoMem instance = null;

    private OrderDaoMem() {

    }

    public static OrderDaoMem getInstance() {
        if (instance == null) {
            instance = new OrderDaoMem();
        }
        return instance;
    }

    @Override
    public void add(Order order) {
        DATA.add(order);
    }

    @Override
    public void clearAll() {
        DATA.clear();

    }

    @Override
    public ArrayList<Order> getOrdersByCustomerId(int customer_id) {
        return null;
    }

    @Override
    public List<Order> getShoppingCartContent(){
        return DATA;
    }

}
