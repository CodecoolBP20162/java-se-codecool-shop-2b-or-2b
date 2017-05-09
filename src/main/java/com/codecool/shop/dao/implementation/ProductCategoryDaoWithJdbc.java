package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kata on 2017.05.09..
 */
public class ProductCategoryDaoWithJdbc implements ProductCategoryDao {

    private static ProductCategoryDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected ProductCategoryDaoWithJdbc() {
    }

    public static ProductCategoryDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new ProductCategoryDaoWithJdbc();
        }
        return instance;
    }


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
            String query = " INSERT INTO product_categories (id, name, department, description) " +
                    "VALUES ('" + id + "','" + category.getName() + "', '" + category.getDepartment() + "', '"
                    + category.getDescription() + "');";
            try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                category.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ProductCategory find(int id) {
        ProductCategory category = null;
        String query = "SELECT * FROM product_categories WHERE id ='" + id + "';";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement())
        {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {

                category = new ProductCategory(result.getString("name"), result.getString("department"),
                        result.getString("description"));
                category.setId(id);
                category.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(category));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;
    }

    @Override
    public ProductCategory find(String name) {
        ProductCategory category = null;
        String query = "SELECT * FROM product_categories WHERE name ='" + name + "';";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement())
        {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {

                category = new ProductCategory(name, result.getString("department"),
                        result.getString("description"));
                category.setId(result.getInt("id"));
                category.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(category));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return category;

    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM product_categories WHERE id='" + id + "';";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement())
        {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ProductCategory> getAll() {
        List<ProductCategory> allCategories = new ArrayList<>();
        String query = "SELECT * FROM product_categories;";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                ProductCategory current = new ProductCategory(result.getString("name"),
                        result.getString("department"), result.getString("description"));
                current.setId(result.getInt("id"));
                current.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(current));
                allCategories.add(current);
            }
            return allCategories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    }
}
