package com.codecool.shop;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kata on 2017.05.11..
 */
class MockProductCategoryTest {

    ProductCategoryDao productCategoryDataStore;

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

        productCategoryDataStore = ProductCategoryDaoWithJdbc.getInstance().setConnectionProvider(mockConnectionProvider);
    }


    @Test
    void add_setsProductCategoryId() throws SQLException {
        ProductCategory mushrooms = new ProductCategory("mushrooms", "Edibles",
                "Thing to eat with weird taste, sometimes poisonous");

        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        productCategoryDataStore.add(mushrooms);

        assertEquals(mushrooms.getId(), 1);
    }

    @Test
    void add_executesValidInsertStatement() throws SQLException {
        ProductCategory mushrooms = new ProductCategory("mushrooms", "Edibles",
                "Thing to eat with weird taste, sometimes poisonous");

        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        productCategoryDataStore.add(mushrooms);

        String expect = "INSERT INTO product_categories (id, name, department, description)" +
                "VALUES ('1','mushrooms', 'Edibles', 'Thing to eat with weird taste, sometimes poisonous');";

        verify(mockStatement).executeUpdate(expect);
    }


    void findById_executesValidInsertStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productCategoryDataStore.find(1);

        String expect = "SELECT * FROM product_categories WHERE id='1';";

        verify(mockStatement).executeQuery(expect);
    }

    void findByName_executesValidInsertStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productCategoryDataStore.find("Lenovo");

        String expect = "SELECT * FROM product_categories WHERE name='Lenovo';";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void find_searchesProductCategoryByIdNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(productCategoryDataStore.find(100));

    }

    @Test
    void find_searchesProductCategoryByNameNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(productCategoryDataStore.find("abraka"));
    }

    @Test
    void remove_executesValidStatement() throws SQLException {
        productCategoryDataStore.remove(1);
        String expect = "DELETE FROM product_categories WHERE id='1';";

        verify(mockStatement).executeUpdate(expect);
    }


    @Test
    void getAll_executesValidStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        productCategoryDataStore.getAll();

        String expect = "SELECT * FROM product_categories;";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void findById_parsesResultSet() throws SQLException {
        ResultSet mockSet = mock(ResultSet.class);
        //returns a resultset with one element
        when(mockSet.next()).thenReturn(true).thenReturn(false);
        when(mockSet.getString(anyString())).thenReturn("mockString");
        when(mockSet.getInt(anyInt())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockSet);
        ProductCategory actual = productCategoryDataStore.find(1);

        assertEquals(actual.getId(), 1);
        assertEquals(actual.getName(), "mockString");
    }

    @Test
    void findByName_parsesResultSet() throws SQLException {
        ResultSet mockSet = mock(ResultSet.class);
        //returns a resultset with one element
        when(mockSet.next()).thenReturn(true).thenReturn(false);
        when(mockSet.getString(anyString())).thenReturn("mockString");
        when(mockSet.getInt(anyInt())).thenReturn(1);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockSet);
        ProductCategory actual = productCategoryDataStore.find("mockString");

        assertEquals(actual.getId(), 0);
        assertEquals(actual.getName(), "mockString");
    }


    @Test
    void findById_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productCategoryDataStore.find(1);
    }

    @Test
    void findByName_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productCategoryDataStore.find("name");
    }


    @Test
    void getAll_swallowsSqlException() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenThrow(SQLException.class);

        productCategoryDataStore.getAll();
    }

    @Test
    void remove_swallowsSqlException() throws SQLException {
        when(mockStatement.executeUpdate(anyString())).thenThrow(SQLException.class);

        productCategoryDataStore.remove(1);
    }

    @Test
    void clearAll_swallowsSqlException() throws SQLException {
        when(mockStatement.executeUpdate(anyString())).thenThrow(SQLException.class);

        productCategoryDataStore.clearAll();
    }

    @AfterEach
    void tearDown() {
        productCategoryDataStore.clearAll();
    }
}