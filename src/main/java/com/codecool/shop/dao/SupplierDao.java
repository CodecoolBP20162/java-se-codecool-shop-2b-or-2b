package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;

import java.util.List;

/**
 * <h1>SupplierDao interface!</h1>
 * The SupplierDao was implemented in SupplierDaoWithJdbc class.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * - add new supplier to database,
 * - find a supplier by id and name
 * - remove supplier from the database by id
 */
public interface SupplierDao {

    /**
     * The add method saves the supplier's data in the database
     * Catch SQLException if DB connection is failed.
     *
     * @param supplier Supplier object
     */
    void add(Supplier supplier);

    /**
     * The find method is searching in suppliers table and returns the supplier with the given id.
     * Creates a supplier instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of Supplier to find.
     * @return supplier Supplier object with the provided id.
     */
    Supplier find(int id);

    /**
     * The find method is searching in suppliers table and returns the supplier with the given name.
     * Creates a supplier instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name The name of the Supplier to find.
     * @return supplier Supplier object with the provided name.
     */
    Supplier find(String name);

    /**
     * The remove method is deleting entry from suppliers  table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of the Supplier to remove.
     */
    void remove(int id);

    /**
     * The getAll method runs a query to get all entries from suppliers table.
     *
     * @return ArrayList<Supplier> A list of Supplier objects from the suppliers table.
     */
    List<Supplier> getAll();


    /**
     * The clearAll method creates a query to delete all entries from suppliers table.
     * We only use it in the tests.
     * Catch SQLException if DB connection or query fails.
     */
    void clearAll();

}
