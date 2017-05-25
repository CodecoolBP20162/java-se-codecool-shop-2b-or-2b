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


    public ProductDao setConnectionProvider(DbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    private DbConnectionProvider connectionProvider = () -> {
        try {
            return DBController.getConnection();
        } catch (SQLException e) {
            return null;
        }
    };

    private Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }




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
            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                product.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Product find(int id) {
        String query = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.id ='" + id + "';";
        Product product = null;

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
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

    public Product find(String name) {
        String query = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.name ='" + name + "';";

        Product product = null;
        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
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

    @Override
    public void remove(int id) {
        String query = "DELETE FROM products WHERE id = '" + id + "';";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT * FROM products;";

        return queryExecuteHandler(query);
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT id FROM products WHERE supplier=" + supplier.getId() + ";";

        return queryExecuteHandler(query);
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT id FROM products WHERE product_category=" + productCategory.getId() + ";";

        return queryExecuteHandler(query);
    }

    @Override
    public void clearAll() {
        String query = "DELETE FROM products;";

        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private ArrayList<Product> queryExecuteHandler(String query) {
        ArrayList<Product> allProducts = new ArrayList<>();

        try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
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
