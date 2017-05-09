package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kata on 2017.05.09..
 */
public class SupplierDaoWithJdbc implements SupplierDao {


    @Override
    public void add(Supplier supplier) {
        //Inserts a new supplier to the table
        String query = "INSERT INTO supplier (name, description) VALUES ('" + supplier.getName() + "', '"
                + supplier.getDescription() + "');";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Supplier find(int id) {
        //Returns the supplier with the given id in the db
        String query = "SELECT * FROM supplier WHERE id='" + id + "';";
        Supplier supplier = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                supplier = new Supplier(result.getString("name"), result.getString("description"));
                supplier.setId(id);
                supplier.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(supplier));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    @Override
    public Supplier find(String name) {
        //Returns the supplier with the given name in the db
        String query = "SELECT * FROM supplier WHERE name='" + name + "';";
        Supplier supplier = null;
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            if (result.next()) {
                supplier = new Supplier(name, result.getString("description"));
                supplier.setId(result.getInt("id"));
                supplier.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(supplier));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;


    }

    @Override
    public void remove(int id) {
        String query = "DELETE FROM supplier WHERE id='" + id + "';";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<Supplier> getAll() {
        ArrayList<Supplier> suppliers = new ArrayList();
        String query = "SELECT * FROM supplier;";
        try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);
            while (result.next()) {
                Supplier supplier = new Supplier(result.getString("name"), result.getString("description"));
                supplier.setId(result.getInt("id"));
                supplier.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(supplier));
                suppliers.add(supplier);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;

    }
}

