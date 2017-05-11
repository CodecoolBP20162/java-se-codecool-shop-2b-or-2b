package com.codecool.shop;

import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kata on 2017.05.10..
 */
class ProductCategoryDaoWithJdbcTest {

    ProductCategory vegetables;
    ProductCategory fruits;

    ProductDaoWithJdbc productDataStore = ProductDaoWithJdbc.getInstance();
    SupplierDaoWithJdbc supplierDataStore = SupplierDaoWithJdbc.getInstance();
    ProductCategoryDaoWithJdbc productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();

    @Test
    void add_addsNewProductCategoryToDatabase() {
        setUp();
        ProductCategory mushrooms = new ProductCategory("mushrooms", "Edibles",
                "Thing to eat with weird taste, sometimes poisonous");
        productCategoryDataStore.add(mushrooms);

        assertNotNull(productCategoryDataStore.find("mushrooms"));

        tearDown();
    }

    @Test
    void find_findsProductCategoryById() {
        setUp();
        int productCategoryId = fruits.getId();
        ProductCategory foundProductCategory = productCategoryDataStore.find(productCategoryId);

        assertEquals(productCategoryId, foundProductCategory.getId());

        tearDown();
    }

    @Test
    void find_findsProductCategoryByName() {
        setUp();
        String productCategoryName = fruits.getName();
        ProductCategory foundProductCategory = productCategoryDataStore.find(productCategoryName);

        assertEquals(productCategoryName, foundProductCategory.getName());

        tearDown();
    }

    @Test
    void remove_removesProductCategoryFromDb() {
        setUp();
        productCategoryDataStore.remove(1);

        assertNull(productCategoryDataStore.find(1));

        tearDown();

    }

    @Test
    void getAll_returnsAllProductCategories() {
        setUp();
        List<ProductCategory> allProductCategories = Arrays.asList(fruits, vegetables);

        assertEquals(allProductCategories.size(), productCategoryDataStore.getAll().size());

        tearDown();
    }

    private void setUp() {
        tearDown();
        fruits = new ProductCategory("fruits", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(fruits);
        vegetables = new ProductCategory("vegetables", "Hardware", "A portable computer with little weight and long battery life.");
        productCategoryDataStore.add(vegetables);
    }

    private void tearDown() {
        productDataStore.clearAll();
        supplierDataStore.clearAll();
        productCategoryDataStore.clearAll();
    }

}