package com.codecool.shop;

import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


/**
 * Created by kata on 2017.05.10..
 */
class ProductDaoWithJdbcTest {

    ProductDaoWithJdbc productDataStore = ProductDaoWithJdbc.getInstance();
    SupplierDaoWithJdbc supplierDataStore = SupplierDaoWithJdbc.getInstance();
    ProductCategoryDaoWithJdbc productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();

    Supplier gyumolcsos;
    Supplier zoldseges;
    ProductCategory gyumolcs;
    ProductCategory zoldseg;
    Product product1;
    Product product2;
    Product product3;


    @Test
    void add_addsNewProductToDatabase() {
        setUp();
        Product product4 = new Product("brokkoli", 50, "USD",
                "Healthy stuff", zoldseg, zoldseges);
        productDataStore.add(product4);

        assertNotNull(productDataStore.find("brokkoli"));

        tearDown();
    }


    @Test
    void find_findsProductBasedOnId() {
        setUp();
        int productId = product1.getId();
        Product foundProduct = productDataStore.find(productId);

        assertEquals(productId, foundProduct.getId());

        tearDown();
    }

    @Test
    void find_findsProductBasedOnName() {
        setUp();
        String productName = product1.getName();
        Product foundProduct = productDataStore.find(productName);

        assertEquals(productName, foundProduct.getName());

        tearDown();
    }

    @Test
    void remove_removesProductFromDatabaseBasedOnId() {
        setUp();
        productDataStore.remove(1);

        assertNull(productDataStore.find(1));

        tearDown();
    }

    @Test
    void getAll_() {
        setUp();
        List<Product> allProducts = Arrays.asList(product1, product2, product3);

        assertEquals(allProducts.size(),
                productDataStore.getAll().size());

        tearDown();
    }

    @Test
    void getBy_findsProductsBySupplier() {
        setUp();
        List<Product> allProductBySupplier = Arrays.asList(product1, product2);

        assertEquals(allProductBySupplier.toString(),
                productDataStore.getBy(gyumolcsos).toString());

        tearDown();

    }

    @Test
    void getBy_findsProductsByProductCategory() {
        setUp();
        List<Product> allProductByProductCategory = Arrays.asList(product3);

        assertEquals(allProductByProductCategory.toString(),
                productDataStore.getBy(zoldseg).toString());

        tearDown();
    }

    public void setUp() {

        tearDown();
        gyumolcsos = new Supplier("gyumolcsos", "Fruity guy");
        supplierDataStore.add(gyumolcsos);
        zoldseges = new Supplier("zoldseges", "Person with vegetables");
        supplierDataStore.add(zoldseges);

        gyumolcs = new ProductCategory("Gyumolcs", "Hardware",
                "Colorful stuff.");
        productCategoryDataStore.add(gyumolcs);
        zoldseg = new ProductCategory("Zoldseg", "Hardware",
                "Green stuff.");
        productCategoryDataStore.add(zoldseg);

        product1 = new Product("alma", 50, "USD",
                "Tasty", gyumolcs, gyumolcsos);
        productDataStore.add(product1);
        product2 = new Product("korte", 479, "USD",
                "Dont like it. Hurts teeths", gyumolcs, gyumolcsos);
        productDataStore.add(product2);
        product3 = new Product("repa", 89, "USD",
                "Fav thing of bunnies.", zoldseg, zoldseges);
        productDataStore.add(product3);

    }

    void tearDown() {
        productDataStore.clearAll();
        supplierDataStore.clearAll();
        productCategoryDataStore.clearAll();
    }
}