package com.codecool.shop.model;

import com.codecool.shop.dao.implementation.ProductDaoMem;

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
            System.out.print("OK");
        }
        return instance;

    }

    public static void setInstanceToNull(ShoppingCart shoppingCart){
        instance = null;
    }

    public void setCartItems(LineItem lineItem){
        cartItems.add(lineItem);
    }

    public List<LineItem> getCartItems(){
        return cartItems;
    }

    public Product findProductById(int id){
        List<LineItem> cartItems = getCartItems();
        for(LineItem i : cartItems){
            if(i.getProduct().getId() == id){
                return i.getProduct();
            } else{
                continue;
            }
        }
        return null;
    }

    public LineItem findLineItemById(int id) {
        List<LineItem> cartItems = getCartItems();
        for (LineItem i : cartItems) {
            if(i.getProduct().getId() == id) {
                return i;
            }
        }
        return null;
    }

    public void deleteProductById(int id){
        LineItem itemToDelete = findLineItemById(id);
        cartItems.remove(itemToDelete);
    };

    public void handleAddToCart (int id) {
        Product foundItem = findProductById(id);
        if (foundItem == null) {
            Product prod = ProductDaoMem.getInstance().find(id);
            LineItem newlineItem = new LineItem(prod);
            setCartItems(newlineItem);
        } else {
            LineItem foundLineItem = findLineItemById(id);
            foundLineItem.raiseQuantity();
        }
    }

    @Override
    public String toString() {
        return "ShoppingCart{" +
                "cartItems=" + cartItems +
                '}';
    }
}
