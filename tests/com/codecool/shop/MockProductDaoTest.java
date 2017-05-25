package com.codecool.shop;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.DbConnectionProvider;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


/**
 * Created by kata on 2017.05.10..
 */

class MockProductDaoTest {

    ProductDao productDataStore;

    Supplier gyumolcsos;
    Supplier zoldseges;
    ProductCategory gyumolcs;
    ProductCategory zoldseg;

    @Mock
    private DbConnectionProvider mockConnectionProvider;

    @Mock
    java.sql.Connection mockConnection;

    @Mock
    private java.sql.Statement mockStatement;

    @BeforeEach
    void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(mockConnectionProvider.getConnection()).thenReturn(mockConnection);
        when(mockConnection.createStatement()).thenReturn(mockStatement);

        gyumolcsos = new Supplier("gyumolcsos", "Fruity guy");

        zoldseges = new Supplier("zoldseges", "Person with vegetables");


        gyumolcs = new ProductCategory("Gyumolcs", "Hardware",
                "Colorful stuff.");

        zoldseg = new ProductCategory("Zoldseg", "Hardware",
                "Green stuff.");

        productDataStore = ProductDaoWithJdbc.getInstance().setConnectionProvider(mockConnectionProvider);
    }


    @Test
    void add_setsProductId() throws SQLException {
        Product product4 = new Product("brokkoli", 50, "USD",
                "Healthy stuff", zoldseg, zoldseges);
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        productDataStore.add(product4);

        assertEquals(product4.getId(), 1);
    }

    @Test
    void add_executesValidInsertStatement() throws SQLException {
        Product product4 = new Product("brokkoli", 50, "USD",
                "Healthy stuff", zoldseg, zoldseges);
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.add(product4);

        String expect = "INSERT INTO products " +
                "(id, name, default_price, currency, description, supplier, product_category)" +
                "VALUES ('1','brokkoli', '50', 'USD', 'Healthy stuff', '0', '0');";

        verify(mockStatement).executeUpdate(expect);
    }


    @Test
    void find_executesValidInsertStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.find(1);

        String expect = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.id ='1';";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void findById_parsesResultSet() throws SQLException {
        ResultSet mockSet = mock(ResultSet.class);
        //returns a resultset with one element
        when(mockSet.next()).thenReturn(true).thenReturn(false);
        when(mockSet.getString(anyString())).thenReturn("USD");
        when(mockSet.getInt(anyInt())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockSet);
        Product actual = productDataStore.find(1);

        assertEquals(actual.getId(), 1);
        assertEquals(actual.getName(), "USD");
    }

    @Test
    void findByName_parsesResultSet() throws SQLException {
        ResultSet mockSet = mock(ResultSet.class);
        //returns a resultset with one element
        when(mockSet.next()).thenReturn(true).thenReturn(false);
        when(mockSet.getString(anyString())).thenReturn("USD");
        when(mockSet.getInt(anyInt())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockSet);
        Product actual = productDataStore.find("name");

        assertEquals(actual.getId(), 0);
        assertEquals(actual.getName(), "USD");
    }



    @Test
    void find_searchesProductsByIdNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(productDataStore.find(100));
    }

    @Test
    void find_findsProductBasedOnName() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.find("repa");

        String expect = "SELECT product_categories.name AS pc_name, products.name AS p_name, suppliers.name AS s_name, * " +
                "FROM products LEFT JOIN product_categories ON products.product_category=product_categories.id " +
                "LEFT JOIN suppliers ON products.supplier=suppliers.id WHERE products.name ='repa';";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void find_searchesProductsByNameNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(productDataStore.find("abraka"));
    }


    @Test
    void remove_executesValidStatement() throws SQLException {
        productDataStore.remove(1);
        String expect = "DELETE FROM products WHERE id = '1';";

        verify(mockStatement).executeUpdate(expect);
    }

    @Test
    void getAll_executesValidStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.getAll();

        String expect = "SELECT * FROM products;";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void getBy_findsProductsBySupplier() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.getBy(gyumolcsos);

        String expect = "SELECT id FROM products WHERE supplier=" + gyumolcsos.getId() + ";";

        verify(mockStatement).executeQuery(expect);

    }

    @Test
    void getBy_findsProductsByProductCategory() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productDataStore.getBy(gyumolcs);

        String expect = "SELECT id FROM products WHERE product_category=" + gyumolcs.getId() + ";";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void findById_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productDataStore.find(1);
    }

    @Test
    void findByName_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productDataStore.find("name");
    }

    @Test
    void getAll_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productDataStore.getAll();
    }

    @Test
    void remove_swallowsSqlException() throws SQLException {
        when(mockStatement.executeUpdate(anyString())).thenThrow(SQLException.class);

        productDataStore.remove(1);
    }

    @Test
    void clearAll_swallowsSqlException() throws SQLException {
        when(mockStatement.executeUpdate(anyString())).thenThrow(SQLException.class);

        productDataStore.clearAll();
    }


    @AfterEach
    public void tearDown() {
        productDataStore.clearAll();
    }
}