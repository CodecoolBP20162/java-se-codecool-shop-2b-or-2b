package com.codecool.shop.model;

/**
 * Created by joker on 2017.04.25..
 */
public class Order {
    private ShoppingCart shoppingCart;
    private User user;

    public Order(ShoppingCart shoppingCart, User user){
        this.shoppingCart = shoppingCart;
        this.user = user;
    }
}
