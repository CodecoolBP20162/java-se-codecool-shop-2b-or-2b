package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Created by kata on 2017.05.09..
 * <h1>SupplierDaoWithJdbc class!</h1>
 * The SupplierDaoWithJdbc class implements the SupplierDao interface.
 * Singleton class, can be created only one instance.
 * You can do some database action:
 * add new supplier to database,
 * find a supplier by an id
 * find a supplier by name
 * remove supplier from the database by id
 */
public class SupplierDaoWithJdbc implements SupplierDao {


    private static SupplierDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected SupplierDaoWithJdbc() {
    }

    public static SupplierDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new SupplierDaoWithJdbc();
        }
        return instance;
    }

    /**
     * The add method saves the supplier's data in the database
     * Catch SQLException if DB connection is failed.
     *
     * @param supplier object
     */
    @Override
    public void add(Supplier supplier) {
        int id;
        ArrayList<Supplier> existingSuppliers = getAll();

        if (find(supplier.getName()) == null) {

            if (existingSuppliers.size() != 0) {
                id = existingSuppliers.size() + 1;
            } else {
                id = 1;
            }

            String query = "INSERT INTO suppliers (id, name, description)" +
                    "VALUES ('" + id + "','" + supplier.getName() + "', '" + supplier.getDescription() + "');";

            try (Connection connection = DBController.getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                supplier.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The find method is searching in suppliers table and returns the supplier with the given id.
     * Creates a supplier instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id
     * @return supplier
     */
    @Override
    public Supplier find(int id) {
        //Returns the supplier with the given id in the db
        String query = "SELECT * FROM suppliers WHERE id='" + id + "';";
        Supplier supplier = null;

        try (Connection connection = DBController.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            if (result.next()) {
                supplier = new Supplier(result.getString("name"),
                        result.getString("description"));
                supplier.setId(id);
                supplier.setProducts((ArrayList<Product>) new ProductDaoWithJdbc().getBy(supplier));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplier;
    }

    /**
     * The find method is searching in suppliers table and returns the supplier with the given name.
     * Creates a supplier instance with the acquired data and returns it.
     * Catch SQLException if DB connection or query fails.
     *
     * @param name
     * @return supplier
     */
    @Override
    public Supplier find(String name) {
        //Returns the supplier with the given name in the db
        String query = "SELECT * FROM suppliers WHERE name='" + name + "';";
        Supplier supplier = null;

        try (Connection connection = DBController.getConnection();
             Statement statement = connection.createStatement()) {
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

    /**
     * The remove method is deleting entry from suppliers  table by id.
     * Catch SQLException if DB connection or query fails.
     *
     * @param id
     */
    @Override
    public void remove(int id) {
        String query = "DELETE FROM suppliers WHERE id='" + id + "';";

        try (Connection connection = DBController.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a query to delete all entries from suppliers table.
     * Catch SQLException if DB connection or query fails.
     */
    @Override
    public void clearAll() {
        String query = "DELETE FROM suppliers;";

        try (Connection connection = DBController.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a query to get all entries from suppliers table.
     *
     * @return ArrayList<Supplier>
     */
    @Override
    public ArrayList<Supplier> getAll() {
        ArrayList<Supplier> suppliers = new ArrayList();
        String query = "SELECT * FROM suppliers;";

        try (Connection connection = DBController.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = statement.executeQuery(query);

            while (result.next()) {
                Supplier supplier = new Supplier(result.getString("name"),
                        result.getString("description"));
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

