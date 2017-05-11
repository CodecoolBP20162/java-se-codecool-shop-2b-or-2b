package com.codecool.shop.dao;

import com.codecool.shop.model.LineItem;
import com.codecool.shop.model.OrderedItems;

import java.util.List;

/**
 * Created by joker on 2017.05.10..
 */
public interface OrderedItemsDao {
    void add(int id, LineItem lineItem);
    void clearAll();
    List<OrderedItems> getByOrderId(int id);
}
