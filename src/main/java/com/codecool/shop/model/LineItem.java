package com.codecool.shop.model;

import com.google.gson.annotations.Expose;
import io.gsonfire.annotations.ExposeMethodResult;

/**
 * Created by joker on 2017.04.25..
 */
public class LineItem {

    @Expose
    private Product product;
    @Expose
    private int quantity;

    public LineItem(Product product) {
        this.product = product;
        this.quantity = 1;
    }

    public Product getProduct() {
        return product;
    }

    @ExposeMethodResult("price")
    public float calculatePrice() {
        return quantity * product.getDefaultPrice();
    }

    public void raiseQuantity() {
        quantity++;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int newQuantity) {
        quantity = newQuantity;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", price=" + calculatePrice() +
                '}';
    }
}
