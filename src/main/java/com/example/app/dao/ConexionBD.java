package com.example.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/galeria_pulseras";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    public static Connection getConnection() throws SQLException {
        // Verificar y crear BD si no existe
        try (Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres", USER, PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE galeria_pulseras");
        } catch (SQLException e) {
            if (!e.getMessage().contains("ya existe")) {
                throw e;
            }
        }
        return DriverManager.getConnection(URL, USER, PASS);
    }
}