package com.codecool.shop.dao.implementation;

import com.codecool.shop.controller.DBController;
import com.codecool.shop.dao.ShoppingCartDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ShoppingCart;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Created by joker on 2017.05.10..
 */
public class ShoppingCartDaoWithJdbc implements ShoppingCartDao {

    private static ShoppingCartDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected ShoppingCartDaoWithJdbc() {
    }

    public static ShoppingCartDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new ShoppingCartDaoWithJdbc();
        }
        return instance;
    }


    @Override
    public void add(ShoppingCart shoppingCart) {
        int id;
        List<ShoppingCart> existingShoppingCarts = getAll();
        List<LineItem> shoppingCartContent = shoppingCart.getCartItems();
        for (LineItem lineItem : shoppingCartContent) {
            if (find(lineItem.) == null) {
                if (existingShoppingCarts.size() != 0) {
                    id = existingShoppingCarts.size() + 1;
                } else {
                    id = 1;
                }
                String query = "INSERT INTO products (id, name, default_price, currency, description," +
                        " supplier, product_category) VALUES ('" + id + "','" + product.getName() + "', '" + product.getDefaultPrice()
                        + "', '" + product.getDefaultCurrency() + "', '" + product.getDescription() + "', '"
                        + product.getSupplier().getId() + "', '" + product.getProductCategory().getId() + "');";
                try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query);
                    product.setId(id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void remove(int id) {

    }
}
