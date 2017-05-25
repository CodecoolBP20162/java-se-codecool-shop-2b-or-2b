package com.codecool.shop.dao;

import com.codecool.shop.model.ProductCategory;

import java.util.List;

/**
 * Created by kata on 2017.05.09..
 * <h1>ProductCategoryDao interface!</h1>
 * The ProductCategoryDao interface was implemented in the ProductCategoryDaoWithJdbc class.
 * You can do some database action(e.g.):
 * - add new productcategory to database,
 * - find a productcategory by an id
 * - find a productcategory by name
 * - remove productcategory from the database by id
 */
public interface ProductCategoryDao {

    /**
     * The add method saves the productcategory's data in the database.
     * Catch SQLException if DB connection is failed.
     *
     * @param category ProductCategory object
     */
    void add(ProductCategory category);

    /**
     * The find method is searching in product_categories table and returns the productCategory with the given id.
     * Creates a productCategory instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of ProductCategory to find.
     * @return category ProductCategory object with the provided id.
     */
    ProductCategory find(int id);

    /**
     * The find method is searching in product_categories table and returns the productCategory with the given name.
     * Creates a productCategory instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name The name of the ProductCategory to find.
     * @return category ProductCategory object with the provided name.
     */
    ProductCategory find(String name);

    /**
     * The remove method is deleting entry from product_categories table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of the ProductCategory to remove.
     */
    void remove(int id);

    /**
     * The getAll method runs a query to get all entries from product_categories table.
     *
     * @return List<ProductCategory> A list of ProductCategories from the product_categories table.
     */
    List<ProductCategory> getAll();

    /**
     * The clearAll method creates a query to delete all entries from product_categories table.
     * We only use it in the tests.
     * Catch SQLException if DB connection or query fails.
     */
    void clearAll();


}
