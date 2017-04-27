package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.04.25..
 */
public class User {
    private List<Order> orders= new ArrayList<>();
    private String name;
    private String email;
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;

    User(String name, String email, String phoneNumber, String billingAddress, String shippingAddress){
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public void setOrders(Order order){
        orders.add(order);
    }

    public String getName(){return name;}

    public static User createUser(String name, String email, String phoneNumber, String billingAddress, String shippingAddress){
        User newUser = new User(name, email, phoneNumber, billingAddress, shippingAddress);
        return newUser;
    }
}
