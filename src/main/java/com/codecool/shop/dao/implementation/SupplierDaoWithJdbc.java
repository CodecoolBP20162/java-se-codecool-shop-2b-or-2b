package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import org.apache.commons.lang3.ObjectUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kata on 2017.05.09..
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

    public SupplierDao setConnectionProvider(DbConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
        return this;
    }

    private DbConnectionProvider connectionProvider = DBController.getInstance();

    private Connection getConnection() throws SQLException {
        return connectionProvider.getConnection();
    }


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

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate(query);
                supplier.setId(id);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Supplier find(int id) {
        //Returns the supplier with the given id in the db
        String query = "SELECT * FROM suppliers WHERE id='" + id + "';";
        Supplier supplier = null;

        try (Connection connection = getConnection();
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

    @Override
    public Supplier find(String name) {
        //Returns the supplier with the given name in the db
        String query = "SELECT * FROM suppliers WHERE name='" + name + "';";
        Supplier supplier = null;

        try (Connection connection = getConnection();
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

    @Override
    public void remove(int id) {
        String query = "DELETE FROM suppliers WHERE id='" + id + "';";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAll() {
        String query = "DELETE FROM suppliers;";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ArrayList<Supplier> getAll() {
        ArrayList<Supplier> suppliers = new ArrayList();
        String query = "SELECT * FROM suppliers;";

        try (Connection connection = getConnection();
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

