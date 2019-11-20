package com.squeaker.entry.utils;

import java.sql.*;

public class DB {
    private static DB conn;

    private static final String CLASS_PATH = "com.mysql.cj.jdbc.Driver";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/squeaker?useSSL=false&serverTimezone=UTC";
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "dladydtjdtjd1";

    private DB() {}

    public Connection connect() {//DB연결, 연결 후 conn 리턴
        try {
            Class.forName(CLASS_PATH);
            return DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DB getInstance() {
        if (conn == null) {
            conn = new DB();
        }
        return conn;
    }

}
