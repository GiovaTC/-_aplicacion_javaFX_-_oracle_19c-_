package com.example.oracleregistros;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    // Ajustar
    private static final String URL = "jdbc:oracle:thin:@//localhost:1521/orcl";
    private static final String USER = "system"; // Cambiar
    private static final String PASS = "Tapiero123"; // Cambiar

    public static Connection getConnection() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
