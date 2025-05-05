package com.example.app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL = "jdbc:postgresql://localhost:5432/";
    private static final String DB_NAME = "galeria_pulseras";
    private static final String USER = "postgres"; // Usuario por defecto de PostgreSQL
    private static final String PASS = "postgres"; // Cambia esto por tu password real

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("Driver PostgreSQL no encontrado: " + e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL + DB_NAME, USER, PASS);
        } catch (SQLException e) {
            if ("3D000".equals(e.getSQLState())) {
                crearBaseDatos();
                return DriverManager.getConnection(URL + DB_NAME, USER, PASS);
            }
            throw e;
        }
    }


    private static void crearBaseDatos() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL + "postgres", USER, PASS);
             Statement stmt = conn.createStatement()) {

            System.out.println("Creando base de datos...");
            stmt.executeUpdate("CREATE DATABASE " + DB_NAME);

            System.out.println("Base de datos creada exitosamente");
        }
    }
}