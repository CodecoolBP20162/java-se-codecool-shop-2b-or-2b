package com.codecool.shop;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;


/**
 * Created by kata on 2017.05.10..
 */
class ProductDaoWithJdbcTest {

    ProductDao productDataStore = ProductDaoWithJdbc.getInstance();
    SupplierDao supplierDataStore = SupplierDaoWithJdbc.getInstance();
    ProductCategoryDao productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();

    Product product1;
    Product product2;
    Product product3;

    @Mock
    Supplier mockGyumolcsos;
    Supplier mockZoldseges;
    ProductCategory mockGyumolcs;
    ProductCategory mockZoldseg;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        tearDown();

        product1 = new Product("alma", 50, "USD",
                "Tasty", mockGyumolcs, mockGyumolcsos);
        productDataStore.add(product1);
        product2 = new Product("korte", 479, "USD",
                "Dont like it. Hurts teeths", mockGyumolcs, mockGyumolcsos);
        productDataStore.add(product2);
        product3 = new Product("repa", 89, "USD",
                "Fav thing of bunnies.", mockZoldseg, mockZoldseges);
        productDataStore.add(product3);

    }


    @Test
    void add_addsNewProductToDatabase() {
        Product product4 = new Product("brokkoli", 50, "USD",
                "Healthy stuff", mockZoldseg, mockZoldseges);
        productDataStore.add(product4);

        assertNotNull(productDataStore.find("brokkoli"));

        tearDown();
    }


    @Test
    void find_findsProductBasedOnId() {
        int productId = product1.getId();
        Product foundProduct = productDataStore.find(productId);

        assertEquals(productId, foundProduct.getId());

        tearDown();
    }

    @Test
    void find_searchesProductsByIdNotPresent_returnsNull() {
        assertNull(productDataStore.find(100));

        tearDown();
    }


    @Test
    void find_findsProductBasedOnName() {
        String productName = product1.getName();
        Product foundProduct = productDataStore.find(productName);

        assertEquals(productName, foundProduct.getName());

        tearDown();
    }

    @Test
    void find_searchesProductsByNameNotPresent_returnsNull() {
        assertNull(productDataStore.find("abraka"));

        tearDown();
    }


    @Test
    void remove_removesProductFromDatabaseBasedOnId() {
        productDataStore.remove(1);

        assertNull(productDataStore.find(1));

        tearDown();
    }

    @Test
    void getAll_() {
        List<Product> allProducts = Arrays.asList(product1, product2, product3);

        assertEquals(allProducts.size(),
                productDataStore.getAll().size());

        tearDown();
    }

    @Test
    void getBy_findsProductsBySupplier() {
        List<Product> allProductBySupplier = Arrays.asList(product1, product2);

        assertEquals(allProductBySupplier.toString(),
                productDataStore.getBy(mockGyumolcsos).toString());

        tearDown();

    }

    @Test
    void getBy_findsProductsByProductCategory() {
        List<Product> allProductByProductCategory = Arrays.asList(product3);

        assertEquals(allProductByProductCategory.toString(),
                productDataStore.getBy(mockZoldseg).toString());

        tearDown();
    }


    void tearDown() {
        productDataStore.clearAll();
        supplierDataStore.clearAll();
        productCategoryDataStore.clearAll();
    }
}