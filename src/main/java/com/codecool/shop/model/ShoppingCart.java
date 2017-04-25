package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.04.25..
 */
public class ShoppingCart {
    private List<LineItem> cartItems = new ArrayList<>();
    private static ShoppingCart instance = null;

    protected ShoppingCart(){

    }

    public static ShoppingCart getInstance(){
        if(instance == null){
            instance = new ShoppingCart();
        }
        return instance;

    }

    public void setCartItems(LineItem lineItem){
        cartItems.add(lineItem);
    }
}
