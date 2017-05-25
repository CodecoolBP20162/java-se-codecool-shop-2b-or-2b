package com.codecool.shop.dao;

import com.codecool.shop.model.Supplier;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.util.List;

/**
    * <h1>ProductDao interface!</h1>
    * The ProductDao was implemented in ProductDaoWithJdbc class.
    * Singleton class, can be created only one instance.
    * You can do some database action:
    * - add new product to database,
    * - find a product by an id and by name
    * - remove product from the database by id
    * - filter products by Supplier, and ProductCategory
 */
public interface ProductDao {

    /**
     * The add method saves the product data in the database.
     * Catch SQLException if DB connection is failed.
     *
     * @param product Product object
     */
    void add(Product product);

    /**
     * The find method is searching in products table and returns the product with the given id.
     * Creates a product instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of Product to find.
     * @return product Product object with the provided id.
     */
    Product find(int id);

    /**
     * The find method is searching in products table and returns the product with the given name.
     * Creates a product instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name The name of the Product to find.
     * @return product Product object with the provided name.
     */
    Product find(String name);

    /**
     * The remove method is deleting entry from products table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of the Product to remove.
     */
    void remove(int id);

    /**
     * The getAll method runs a query to get all entries from products table.
     *
     * @return List<Product> A list of Products from the products table.
     */
    List<Product> getAll();

    /**
     * The getBy method runs a query to get entries from products table filtered by the given Supplier object.
     *
     * @param supplier A Supplier object to filter by.
     * @return List<Product> A list of Product objects, filtered by a Supplier.
     *
     */
    List<Product> getBy(Supplier supplier);

    /**
     * This getBy method runs a query to get entries from products table filtered by the given ProductCategory object.
     *
     * @param productCategory A ProductCategory object to filter by.
     * @return List<Product> A list of Product objects, filtered by a ProductCategory.
     */
    List<Product> getBy(ProductCategory productCategory);

    /**
     * The clearAll method runs a query to delete all entries from products table.
     * Catch SQLException if DB connection or query fails.
     */
    void clearAll();
}
