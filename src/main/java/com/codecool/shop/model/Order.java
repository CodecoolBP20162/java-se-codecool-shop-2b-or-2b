package com.codecool.shop.model;

import java.util.List;

/**
 * Created by joker on 2017.04.25..
 */
public class Order {
    private List<LineItem> shoppingCartContent;


    public Order(List<LineItem> lineItemList){
        this.shoppingCartContent = lineItemList;
    }

    public List<LineItem> getShoppingCartContent(){
        return shoppingCartContent;
    }

}
