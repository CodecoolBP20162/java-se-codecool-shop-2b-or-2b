package com.codecool.shop;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.DbConnectionProvider;
import com.codecool.shop.dao.implementation.ProductCategoryDaoWithJdbc;
import com.codecool.shop.dao.implementation.ProductDaoWithJdbc;
import com.codecool.shop.dao.implementation.SupplierDaoWithJdbc;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kata on 2017.05.10..
 */
class MockSupplierDaoTest {

    SupplierDao supplierDataStore;

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

        supplierDataStore = SupplierDaoWithJdbc.getInstance().setConnectionProvider(mockConnectionProvider);
    }

    @Test
    void add_setsSupplierId() throws SQLException {
        Supplier healthian = new Supplier("healthian",
                "Delivers all kinds of healthy stuff like seeds and grass");

        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        supplierDataStore.add(healthian);

        assertEquals(healthian.getId(), 1);
    }

    @Test
    void add_executesValidInsertStatement() throws SQLException {
        Supplier healthian = new Supplier("healthian",
                "Delivers all kinds of healthy stuff like seeds and grass");

        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        supplierDataStore.add(healthian);

        String expect = "INSERT INTO suppliers " +
                "(id, name, description)" +
                "VALUES ('1','healthian', 'Delivers all kinds of healthy stuff like seeds and grass');";

        verify(mockStatement).executeUpdate(expect);
    }

    void findById_executesValidInsertStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        supplierDataStore.find(1);

        String expect = "SELECT * FROM suppliers WHERE id='1';";

        verify(mockStatement).executeQuery(expect);
    }

    void findByName_executesValidInsertStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        supplierDataStore.find("Lenovo");

        String expect = "SELECT * FROM suppliers WHERE name='Lenovo';";

        verify(mockStatement).executeQuery(expect);
    }

    @Test
    void find_searchesSupplierByIdNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(supplierDataStore.find(100));

    }

    @Test
    void find_searchesSuppliersByNameNotPresent_returnsNull() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));

        assertNull(supplierDataStore.find("abraka"));
    }


    @Test
    void remove_executesValidStatement() throws SQLException {
        supplierDataStore.remove(1);
        String expect = "DELETE FROM suppliers WHERE id='1';";

        verify(mockStatement).executeUpdate(expect);
    }


    @Test
    void getAll_executesValidStatement() throws SQLException {
        when(mockStatement.executeQuery(anyString())).thenReturn(mock(ResultSet.class));
        supplierDataStore.getAll();

        String expect = "SELECT * FROM suppliers;";

        verify(mockStatement).executeQuery(expect);
    }


    @AfterEach
    public void tearDown() {
        supplierDataStore.clearAll();
    }
}