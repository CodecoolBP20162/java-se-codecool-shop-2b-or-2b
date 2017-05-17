package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class ProductController {

    ProductDao productDataStore;
    ProductCategoryDao productCategoryDataStore;
    SupplierDao supplierDataStore;

    public ProductController() {
        productDataStore = ProductDaoWithJdbc.getInstance();
        productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();
        supplierDataStore = SupplierDaoWithJdbc.getInstance();
    }

    private Map<String, Object> createDefaultMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("products", productDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        return params;
    }

    public ModelAndView renderProducts(Request req, Response res) {
        Map<String, Object> params = createDefaultMap();

        return new ModelAndView(params, "product/index");
    }

    public ModelAndView renderPayment(Request req, Response res) {
        String customerId = req.params(":id");

        Map<String, Object> params = createDefaultMap();
        params.put("customer_id", customerId);

        return new ModelAndView(params, "product/payment");
    }

    public ModelAndView renderProductsBySupplier(Request req, Response res) {
        String supplierName = req.params(":name");
        Supplier chosenSupplier = supplierDataStore.find(supplierName);

        Map<String, Object> params = createDefaultMap();
        params.put("products", productDataStore.getBy(chosenSupplier));

        return new ModelAndView(params, "product/index");
    }

    public ModelAndView renderProductsByCategory(Request req, Response res) {
        String categoryName = req.params(":name");
        ProductCategory chosenCategory = productCategoryDataStore.find(categoryName);

        Map<String, Object> params = createDefaultMap();
        params.put("products", productDataStore.getBy(chosenCategory));

        return new ModelAndView(params, "product/index");
    }
}
