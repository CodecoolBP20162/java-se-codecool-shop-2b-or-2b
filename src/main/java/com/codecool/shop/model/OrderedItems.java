package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.05.11..
 */
public class OrderedItems {
    private List<LineItem> shoppingCartContent = new ArrayList<>();

    public OrderedItems() {
        this.shoppingCartContent = ShoppingCart.getInstance().getCartItems();
    }


    public List<LineItem> getShoppingCartContent() {
        return shoppingCartContent;
    }


}
