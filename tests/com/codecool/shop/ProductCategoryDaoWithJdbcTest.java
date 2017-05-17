package com.codecool.shop;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.ProductCategory;
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

    ProductDao productDataStore = ProductDaoWithJdbc.getInstance();
    SupplierDao supplierDataStore = SupplierDaoWithJdbc.getInstance();
    ProductCategoryDao productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();

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
    void find_searchesProductCategoryByIdNotPresent_returnsNull() {
        setUp();

        assertNull(productCategoryDataStore.find(100));

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
    void find_searchesProductCategoryByNameNotPresent_returnsNull() {
        setUp();

        assertNull(productCategoryDataStore.find("abraka"));

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
        fruits = new ProductCategory("fruits", "Hardware", "Colorful tasty stuff");
        productCategoryDataStore.add(fruits);
        vegetables = new ProductCategory("vegetables", "Hardware", "pfejjj");
        productCategoryDataStore.add(vegetables);
    }

    private void tearDown() {
        productDataStore.clearAll();
        supplierDataStore.clearAll();
        productCategoryDataStore.clearAll();
    }

}