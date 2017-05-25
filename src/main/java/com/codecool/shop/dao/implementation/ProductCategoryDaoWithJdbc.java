package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kata on 2017.05.09..
 * <h1>ProductCategoryDaoWithJdbc class!</h1>
 * The ProductCategoryDaoWithJdbc class implements the ProductCategoryDao interface.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * - add new productcategory to database,
 * - find a productcategory by an id
 * - find a productcategory by name
 * - remove productcategory from the database by id
 */
public class ProductCategoryDaoWithJdbc implements ProductCategoryDao {

    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryDaoWithJdbc.class);
    private static ProductCategoryDaoWithJdbc instance = null;

    /** A protected Constructor prevents any other class from instantiating.
     */
    protected ProductCategoryDaoWithJdbc() {
    }

    public static ProductCategoryDaoWithJdbc getInstance() {
        if (instance == null) {
            logger.debug("Creating new {}", ProductCategoryDaoWithJdbc.class.getSimpleName());
            instance = new ProductCategoryDaoWithJdbc();
        }
        return instance;
    }

    public ProductCategoryDao setConnectionProvider(DbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    private DbConnectionProvider connectionProvider = DBController.getInstance();

    private Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }


    /**
     * The add method saves the productcategory's data in the database
     * Catch SQLException if DB connection is failed.
     *
     * @param category ProductCategory object
     */

    @Override
    public void add(ProductCategory category) {
        int id;
        List<ProductCategory> existingCategories = getAll();

        if (find(category.getName()) == null) {

            if (existingCategories.size() != 0) {
                id = existingCategories.size() + 1;
            } else {
                id = 1;
            }
            String query = "INSERT INTO product_categories (id, name, department, description)" +
                    "VALUES ('" + id + "','" + category.getName() + "', '" + category.getDepartment() + "', '"
                    + category.getDescription() + "');";

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                category.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The find method is searching in product_categories table and returns the productCategory with the given id.
     * Creates a productCategory instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of ProductCategory to find.
     * @return category ProductCategory object with the provided id.
     */

    @Override
    public ProductCategory find(int id) {
        ProductCategory category = null;
        String query = "SELECT * FROM product_categories WHERE id ='" + id + "';";

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {

                category = new ProductCategory(result.getString("name"),
                        result.getString("department"),
                        result.getString("description"));
                category.setId(id);
                category.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(category));

            }

        } catch (SQLException e) {
            logger.warn("Category not found in DB");
            e.printStackTrace();
        }
        return category;
    }

    /**
     * The find method is searching in product_categories table and returns the productCategory with the given name.
     * Creates a productCategory instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name The name of the ProductCategory to find.
     * @return category ProductCategory object with the provided name.
     */
    @Override
    public ProductCategory find(String name) {
        ProductCategory category = null;
        String query = "SELECT * FROM product_categories WHERE name ='" + name + "';";

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {

                category = new ProductCategory(name,
                        result.getString("department"),
                        result.getString("description"));
                category.setId(result.getInt("id"));
                category.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(category));

            }
        } catch (SQLException e) {
            logger.warn("Category not found in DB");
            e.printStackTrace();
        }

        return category;

    }

    /**
     * The remove method is deleting entry from product_categories table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id The id of the ProductCategory to remove.
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_categories WHERE id='" + id + "';";

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * The getAll method runs a query to get all entries from product_categories table.
     *
     * @return A list of ProductCategories from the product_categories table.
     */
    @Override
    public List<ProductCategory> getAll() {
        List<ProductCategory> allCategories = new ArrayList<>();
        String query = "SELECT * FROM product_categories;";

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                ProductCategory current = new ProductCategory(result.getString("name"),
                        result.getString("department"),
                        result.getString("description"));
                current.setId(result.getInt("id"));
                current.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(current));
                allCategories.add(current);
            }

            return allCategories;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * The clearAll method creates a query to delete all entries from product_categories table.
     * We only use it in the tests.
     * Catch SQLException if DB connection or query fails.
     */
    @Override
    public void clearAll() {
        String query = "DELETE FROM product_categories;";

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

