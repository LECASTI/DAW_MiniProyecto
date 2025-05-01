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
        // Primero intenta conectar a la BD principal
        try {
            return DriverManager.getConnection(URL + DB_NAME, USER, PASS);
        } catch (SQLException e) {
            if (e.getMessage().contains("No existe la base de datos")) {
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
            stmt.executeUpdate("CREATE DATABASE " + DB_NAME + " WITH OWNER = " + USER +
                    " ENCODING 'UTF8' LC_COLLATE 'en_US.UTF-8' LC_CTYPE 'en_US.UTF-8'");

            System.out.println("Base de datos creada exitosamente");
        }
    }
}