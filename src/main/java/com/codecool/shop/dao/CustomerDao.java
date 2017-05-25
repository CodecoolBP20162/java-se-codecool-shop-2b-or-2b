package com.codecool.shop.dao;

import com.codecool.shop.model.Customer;

/**
 * Created by eb on 2017.05.11..
 * <h1>CustomerDao interface</h1>
 * CustomerDaoWithJdbc class implements this interface.
 * You can do database action:
 * add new customer to database,
 * find customer by Id,
 * find customer by phone number.
 */
public interface CustomerDao {

    /**
     * The add method can save the customer data to database.
     * @param customer The customer instance to save data to database.
     */
    void add(Customer customer);

    /**
     * The find method is searching in customers table and returns the customer with the given id.
     * @param id The id of customer to find in database.
     * @return customer instance.
     */
    Customer find(int id);

    /**
     * The findByPhoneNumber method is searching in customers table and returns the customer's id with the given phoneNumber.
     * @param phoneNumber The phone number of the customer to find in database.
     * @return id of the Customer.
     */
    int findByPhoneNumber(int phoneNumber);
}
