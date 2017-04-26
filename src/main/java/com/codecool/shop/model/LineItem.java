package com.codecool.shop.model;

/**
 * Created by joker on 2017.04.25..
 */
public class LineItem {

    private Product product;
    private int quantity;
    private float price;

    public LineItem(Product product){
        this.product = product;
        this.quantity = 1;
        this.price = calculatePrice();
    }

    public Product getProduct(){return product;}

    public float calculatePrice(){
        return quantity * product.getDefaultPrice();
    }

    public void setQuantity() {
        quantity++;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "product=" + product +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
