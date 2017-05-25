package com.codecool.shop.dao.implementation;

import java.sql.Connection;

public interface DbConnectionProvider {
    Connection getConnection();
}
