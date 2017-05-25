package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * Controls rendering of products.
 * Renders products from database based on filter conditions.
 */
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    ProductDao productDataStore;
    ProductCategoryDao productCategoryDataStore;
    SupplierDao supplierDataStore;


    /**
     * Constructor - creates ProductDaoWithJdbc, SupplierDaoWithJdbc and
     * ProductCategoryDaoWithJdbc instances.
     */
    public ProductController() {
        productDataStore = ProductDaoWithJdbc.getInstance();
        productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();
        supplierDataStore = SupplierDaoWithJdbc.getInstance();
    }

    /**
     * Get all products, supplier and product categories from Database
     * and save in a Map.
     * @return Map (String, Object)
     */
    private Map<String, Object> createDefaultMap() {
        Map<String, Object> params = new HashMap<>();
        params.put("categories", productCategoryDataStore.getAll());
        params.put("products", productDataStore.getAll());
        params.put("suppliers", supplierDataStore.getAll());
        return params;
    }

    /**
     * Renders all products from database.
     * @param req request object
     * @param res response object
     * @return populated ModelAndView
     */
    public ModelAndView renderProducts(Request req, Response res) {
        Map<String, Object> params = createDefaultMap();

        return new ModelAndView(params, "product/index");
    }

    /**
     * Renders payment page for the given customer, based on request params.
     * @param req request object
     * @param res response object
     * @return populated ModelAndView
     */
    public ModelAndView renderPayment(Request req, Response res) {
        String customerId = req.params(":id");

        Map<String, Object> params = createDefaultMap();
        params.put("customer_id", customerId);
        logger.info("Redirect payment page with customer ID: {}", customerId);
        return new ModelAndView(params, "product/payment");
    }

    /**
     * Renders products, based on supplier from request params.
     * @param req request object
     * @param res response object
     * @return populated ModelAndView
     */
    public ModelAndView renderProductsBySupplier(Request req, Response res) {
        String supplierName = req.params(":name");
        Supplier chosenSupplier = supplierDataStore.find(supplierName);

        Map<String, Object> params = createDefaultMap();
        params.put("products", productDataStore.getBy(chosenSupplier));

        return new ModelAndView(params, "product/index");
    }

    /**
     * Renders products, based on productsCategory from request params.
     * @param req request object
     * @param res response object
     * @return populated ModelAndView
     */
    public ModelAndView renderProductsByCategory(Request req, Response res) {
        String categoryName = req.params(":name");
        ProductCategory chosenCategory = productCategoryDataStore.find(categoryName);

        Map<String, Object> params = createDefaultMap();
        params.put("products", productDataStore.getBy(chosenCategory));

        return new ModelAndView(params, "product/index");
    }


}
