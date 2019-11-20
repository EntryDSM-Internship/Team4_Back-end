package com.squeaker.entry.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Query {
    protected Connection connection = DB.getInstance().connect();
    protected Statement statement;
    protected ResultSet resultSet;
    protected String query;

    protected String query(String sql, Object... objects) {
        return String.format(sql, objects);
    }
}
