package com.codecool.shop.model;

/**
 * Created by joker on 2017.04.25..
 */
public class Order {
    private OrderedItems orderedItems;
    private Customer customer;
    private int id;


    public Order(Customer customer) {
        this.customer = customer;
        this.orderedItems = new OrderedItems();
    }

    public Integer calculateTotalPrice(){
        float totalPrice = 0;
        for (LineItem lineItem : orderedItems.getShoppingCartContent()){
            totalPrice += lineItem.calculatePrice();
        }
        return Math.round(totalPrice);

    }

    public OrderedItems getOrderedItems() {
        return orderedItems;
    }
    public Customer getCustomer() {
        return customer;
    }

    public void setId(int newId){
        this.id = newId;
    }
}
