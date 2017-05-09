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

    private static final String DATABASE = "jdbc:postgresql://localhost:5432/codecoolshop";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "postgres";

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


    @Override
    public void add(Product product) {
        String query = "INSERT INTO product (name, default_price, currency, description," +
                " supplier, product_category) VALUES ('"+ product.getName() + "', '" + product.getDefaultPrice()
                + "', '" + product.getDefaultCurrency() + "', '" + product.getDescription() + "', '"
                + product.getSupplier().getId() + "', '" + product.getProductCategory().getId() + "');";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Product find(int id) {
        String query = "SELECT * FROM product LEFT JOIN product_category ON productcategory=id LEFT JOIN supplier ON supplier=id WHERE id ='" + id + "';";
        Product product = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()){
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                ProductCategory category = new ProductCategory(result.getString("name"), result.getString("department"),
                        result.getString("description"));
                category.setId(result.getInt("product_category"));
                Supplier supplier = new Supplier(result.getString("name"), result.getString("description"));
                supplier.setId(result.getInt("supplier"));
                product = new Product(result.getString("name"), result.getFloat("default_price"),
                        result.getString("currency"), result.getString("description"),
                        category, supplier);
                product.setId(id);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return product;
    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product WHERE id = '" + id +"';";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement())
        {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAll() {
        String query = "SELECT id FROM product;";
        return queryExecuteHandler(query);
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        String query = "SELECT id FROM product WHERE supplier=" + supplier.getId() + ";";
        return queryExecuteHandler(query);
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        String query = "SELECT id FROM product WHERE product_category=" + productCategory.getId() + ";";
        return queryExecuteHandler(query);
    }


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
