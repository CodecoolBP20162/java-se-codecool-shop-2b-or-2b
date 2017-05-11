package com.codecool.shop;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by kata on 2017.05.10..
 */
class SupplierDaoWithJdbcTest {

    ProductDao productDataStore = ProductDaoWithJdbc.getInstance();
    SupplierDao supplierDataStore = SupplierDaoWithJdbc.getInstance();
    ProductCategoryDao productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance();

    Supplier fruitarian;
    Supplier vegetarian;

    @Test
    void add_addsNewSupplierToDatabase() {
        setUp();
        Supplier healthian = new Supplier("healthian",
                "Delivers all kinds of healthy stuff like seeds and grass");
        supplierDataStore.add(healthian);

        assertNotNull(supplierDataStore.find("healthian"));

        tearDown();
    }

    @Test
    void find_findsSupplierBasedOnId() {
        setUp();
        int supplierId = fruitarian.getId();
        Supplier foundSupplier = supplierDataStore.find(supplierId);

        assertEquals(supplierId, foundSupplier.getId());

        tearDown();
    }

    @Test
    void find_searchesProductsByIdNotPresent_returnsNull() {
        setUp();

        assertNull(supplierDataStore.find(100));

        tearDown();
    }

    @Test
    void find_findsSupplierBasedOnName() {
        setUp();
        String supplierName = fruitarian.getName();
        Supplier foundSupplier = supplierDataStore.find(supplierName);

        assertEquals(supplierName, foundSupplier.getName());

        tearDown();
    }

    @Test
    void find_searchesProductsByNameNotPresent_returnsNull() {
        setUp();

        assertNull(supplierDataStore.find("abraka"));

        tearDown();
    }

    @Test
    void remove_removesSupplierFromDatabaseBasedOnId() {
        setUp();
        supplierDataStore.remove(1);

        assertNull(supplierDataStore.find(1));

        tearDown();
    }

    @Test
    void getAll_returnsAllSuppliers() {
        setUp();
        List<Supplier> allSuppliers = Arrays.asList(fruitarian, vegetarian);

        assertEquals(allSuppliers.size(), supplierDataStore.getAll().size());

        tearDown();
    }

    public void setUp() {
        tearDown();
        fruitarian = new Supplier("fruitarian", "Computers");
        supplierDataStore.add(fruitarian);
        vegetarian = new Supplier("zoldseges", "Digital content and services");
        supplierDataStore.add(vegetarian);

    }

    public void tearDown() {
        productDataStore.clearAll();
        supplierDataStore.clearAll();
        productCategoryDataStore.clearAll();
    }
}