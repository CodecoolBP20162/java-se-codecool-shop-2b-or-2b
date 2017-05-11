package com.codecool.shop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joker on 2017.04.25..
 */
public class Customer extends Person {
    private List<Order> orders = new ArrayList<>();
    private String phoneNumber;
    private String billingAddress;
    private String shippingAddress;
    private int id;

    public Customer(int id, String name, String email, String phoneNumber, String billingAddress, String shippingAddress) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.billingAddress = billingAddress;
        this.shippingAddress = shippingAddress;
    }

    public static Customer createUser(int id, String name, String email, String phoneNumber, String billingAddress, String shippingAddress) {
        Customer newCustomer = new Customer(id, name, email, phoneNumber, billingAddress, shippingAddress);
        return newCustomer;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public int getId(){return id;}

    public void setOrders(Order order) {
        orders.add(order);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", orders=" + orders +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", shippingAddress='" + shippingAddress + '\'' +
                '}';
    }
}
