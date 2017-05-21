package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kata on 2017.05.08..
 * <h1>ProductDaoWithJdbc class!</h1>
 * The ProductDaoWithJdbc class implements the ProductDao interface.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * add new product to database,
 * find a product by an id
 * find a product by name
 * remove product from the database by id
 */
public class ProductDaoWithJdbc implements ProductDao {

    private static ProductDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected ProductDaoWithJdbc() {
    }

    public static ProductDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new ProductDaoWithJdbc();
        }
        return instance;
    }

    /**
     * The add method saves the product data in the database
     * Catch SQLException if DB connection is failed.
     *
     * @param product object
     */
    @Override
    public void add(Product product) {
        int id;
        List<Product> existingProducts = getAll();
        if (find(product.getName()) == null) {
            if (existingProducts.size() != 0) {
                id = existingProducts.size() + 1;
            } else {
                id = 1;
            }
            String query = "INSERT INTO products (id, name, default_price, currency, description," +
                    " supplier, product_category)" +
                    "VALUES ('" + id + "','"
                    + product.getName() + "', '"
                    + product.getDefaultPrice() + "', '"
                    + product.getDefaultCurrency() + "', '"
                    + product.getDescription() + "', '"
                    + product.getSupplier().getId() + "', '"
                    + product.getProductCategory().getId() + "');";
            try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                product.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The find method is searching in products table and returns the product with the given id.
     * Creates a product instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id
     * @return product
     */

    @Override
    public Product find(int id) {
        String query = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.id ='" + id + "';";
        Product product = null;

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                ProductCategory category = new ProductCategory(result.getString("pc_name"),
                        result.getString("department"),
                        result.getString("description"));
                category.setId(result.getInt("product_category"));
                Supplier supplier = new Supplier(result.getString("s_name"),
                        result.getString("description"));
                supplier.setId(result.getInt("supplier"));
                product = new Product(result.getString("p_name"),
                        result.getInt("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        category, supplier);
                product.setId(id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    /**
     * The find method is searching in products table and returns the product with the given name.
     * Creates a product instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name
     * @return product
     */
    public Product find(String name) {
        String query = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.name ='" + name + "';";

        Product product = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                ProductCategory category = new ProductCategory(result.getString("pc_name"),
                        result.getString("department"),
                        result.getString("description"));
                category.setId(result.getInt("product_category"));
                Supplier supplier = new Supplier(result.getString("s_name"),
                        result.getString("description"));
                supplier.setId(result.getInt("supplier"));
                product = new Product(name, result.getInt("default_price"),
                        result.getString("currency"),
                        result.getString("description"),
                        category, supplier);
                product.setId(result.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return product;
    }

    /**
     * The remove method is deleting entry from products table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM products WHERE id = '" + id + "';";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a query to get all entries from products table.
     *
     * @return List<Product>
     */
    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products;";

        return queryExecuteHandler(query);
    }

    /**
     * Creates a query to get entries from products table filtered by the given supplier object.
     *
     * @param supplier
     * @return List<Product>
     */
    @Override
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT id FROM products WHERE supplier=" + supplier.getId() + ";";

        return queryExecuteHandler(query);
    }

    /**
     * Creates a query to get entries from products table filtered by the given ProductCategory object.
     *
     * @param productCategory
     * @return List<Product>
     */
    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT id FROM products WHERE product_category=" + productCategory.getId() + ";";

        return queryExecuteHandler(query);
    }

    /**
     * Creates a query to delete all entries from products table.
     * Catch SQLException if DB connection or query fails.
     */
    @Override
    public void clearAll() {
        String query = "DELETE FROM products;";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * It gets a query as parameter and it processes the result.
     * Catch SQLException if DB connection or query fails.
     *
     * @param query
     * @return ArrayList<Product>
     */

    private ArrayList<Product> queryExecuteHandler(String query) {
        ArrayList<Product> allProducts = new ArrayList<>();

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Product product = find(result.getInt("id"));
                product.setId(result.getInt("id"));
                allProducts.add(product);
            }
            return allProducts;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

}
