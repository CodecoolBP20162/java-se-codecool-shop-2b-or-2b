package com.codecool.shop.dao;

import com.codecool.shop.model.ShoppingCart;

import java.util.List;

/**
 * Created by joker on 2017.05.10..
 */
public interface ShoppingCartDao {
    void add(ShoppingCart shoppingCart);
    ShoppingCart find(int id);
    void remove(int id);
    List<ShoppingCart> getAll();
}
