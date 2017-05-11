package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.OrderedItemsDao;
import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.OrderedItems;

import java.util.List;

/**
 * Created by joker on 2017.05.10..
 */
public class OrderedItemsDaoWithJdbc implements OrderedItemsDao {

    private static OrderedItemsDaoWithJdbc instance = null;

    /* A private Constructor prevents any other class from instantiating.
     */
    protected OrderedItemsDaoWithJdbc() {
    }

    public static OrderedItemsDaoWithJdbc getInstance() {
        if (instance == null) {
            instance = new OrderedItemsDaoWithJdbc();
        }
        return instance;
    }

    @Override
    public void add(LineItem lineItem) {
        String query = "INSERT INTO ordered_items (id, name, default_price, currency, description," +
                        " supplier, product_category) VALUES ('" + id + "','" + product.getName() + "', '" + product.getDefaultPrice()
                        + "', '" + product.getDefaultCurrency() + "', '" + product.getDescription() + "', '"
                        + product.getSupplier().getId() + "', '" + product.getProductCategory().getId() + "');
    }

//    @Override
//    public void add(Product product) {
//        int id;
//        List<Product> existingShoppingCarts = getAll();
//        List<LineItem> shoppingCartContent = shoppingCart.getCartItems();
//        for (LineItem lineItem : shoppingCartContent) {
//            if (find(lineItem.) == null) {
//                if (existingShoppingCarts.size() != 0) {
//                    id = existingShoppingCarts.size() + 1;
//                } else {
//                    id = 1;
//                }
//                String query = "INSERT INTO products (id, name, default_price, currency, description," +
//                        " supplier, product_category) VALUES ('" + id + "','" + product.getName() + "', '" + product.getDefaultPrice()
//                        + "', '" + product.getDefaultCurrency() + "', '" + product.getDescription() + "', '"
//                        + product.getSupplier().getId() + "', '" + product.getProductCategory().getId() + "');";
//                try (Connection connection = DBController.getConnection(); Statement statement = connection.createStatement()) {
//                    statement.executeUpdate(query);
//                    product.setId(id);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }



    @Override
    public void clearAll() {

    }

    @Override
    public List<OrderedItems> getByOrderId(int id) {
        return null;
    }


//
//
//    @Override
//    public void remove(int id) {
//
//    }
}
