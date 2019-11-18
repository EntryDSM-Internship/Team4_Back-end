package com.squeaker.entry.utils;

import java.sql.*;

public class DB {

    private static final String CLASS_PATH = "com.mysql.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost/squeaker?useSSL=false";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "dladydtjdtjd1";

    public static ResultSet query(String queryText, Object... data) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName(CLASS_PATH);
             connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
             statement = connection.createStatement();

             resultSet = statement.executeQuery(String.format(queryText, data));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(connection != null) connection.close();
                if(statement != null) statement.close();
                if(resultSet != null) resultSet.close();
            } catch (SQLException ignored) {}
        }

        return resultSet;
    }

}
