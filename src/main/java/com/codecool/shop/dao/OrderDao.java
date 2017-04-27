package com.codecool.shop.dao;

import com.codecool.shop.model.Order;

import java.util.List;

/**
 * Created by joker on 2017.04.27..
 */
public interface OrderDao {

    void add(Order order);

    List<Order> getShoppingCartContent();

}
