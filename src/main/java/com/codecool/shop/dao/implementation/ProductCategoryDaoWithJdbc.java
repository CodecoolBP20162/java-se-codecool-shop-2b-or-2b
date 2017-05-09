package com.codecool.shop.dao.implementation;

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
    @Override
    public void add(ProductCategory category) {
        String query = " INSERT INTO product_category (name, department, description) " +
                "VALUES ('" + category.getName() + "', '" + category.getDepartment() + "', '"
                + category.getDescription() + "');";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ProductCategory find(int id) {
        ProductCategory category = null;
        String query = "SELECT * FROM product_category WHERE id ='" + id + "';";
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
        String query = "SELECT * FROM product_category WHERE name ='" + name + "';";
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
        String query = "DELETE FROM product_category WHERE id='" + id + "';";
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
        String query = "SELECT * FROM product_category";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                ProductCategory current = new ProductCategory(result.getString("name"),
                        result.getString("department"), result.getString("description"));
                current.setId(result.getInt("c_id"));
                current.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(current));
                allCategories.add(current);
            }
            return allCategories;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;    }
}
