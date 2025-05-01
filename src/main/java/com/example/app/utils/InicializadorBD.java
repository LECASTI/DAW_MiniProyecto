package com.example.app.utils;

import com.example.app.dao.ConexionBD;
import org.mindrot.jbcrypt.BCrypt;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.*;

public class InicializadorBD implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Inicializando base de datos...");
        try (Connection conn = ConexionBD.getConnection()) {
            System.out.println("Conexión a BD establecida");

            if (!existeTabla(conn, "usuarios")) {
                System.out.println("Creando tablas...");
                ejecutarScriptInicial(conn);

                System.out.println("Insertando datos de prueba...");
                insertarDatosPrueba(conn);

                System.out.println("Base de datos inicializada con datos de prueba");
            } else {
                System.out.println("Las tablas ya existen, omitiendo creación");
            }
        } catch (SQLException e) {
            System.err.println("ERROR durante inicialización de BD:");
            e.printStackTrace();
            throw new RuntimeException("Error inicializando BD: " + e.getMessage(), e);
        }
    }

    private boolean existeTabla(Connection conn, String tabla) throws SQLException {
        DatabaseMetaData meta = conn.getMetaData();
        try (ResultSet rs = meta.getTables(null, null, tabla, new String[]{"TABLE"})) {
            return rs.next();
        }
    }

    private void ejecutarScriptInicial(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            // Creación de tablas
            stmt.executeUpdate(
                    "CREATE TABLE usuarios (" +
                            "usuario_id SERIAL PRIMARY KEY, " +
                            "username VARCHAR(255) UNIQUE NOT NULL, " +
                            "password_hash VARCHAR(255) NOT NULL, " +
                            "rol VARCHAR(50) NOT NULL CHECK (rol IN ('usuario', 'admin', 'superadmin')), " +
                            "creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "ultimo_login TIMESTAMP)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE chats (" +
                            "chat_id SERIAL PRIMARY KEY, " +
                            "usuario_id INTEGER REFERENCES usuarios(usuario_id) ON DELETE CASCADE, " +
                            "titulo VARCHAR(255), " +
                            "estado VARCHAR(50) DEFAULT 'abierto' CHECK (estado IN ('abierto', 'archivado')), " +
                            "creado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE mensajes (" +
                            "mensaje_id SERIAL PRIMARY KEY, " +
                            "chat_id INTEGER REFERENCES chats(chat_id) ON DELETE CASCADE, " +
                            "usuario_id INTEGER REFERENCES usuarios(usuario_id) ON DELETE SET NULL, " +
                            "contenido TEXT NOT NULL, " +
                            "enviado_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "editado BOOLEAN DEFAULT FALSE)"
            );

            stmt.executeUpdate(
                    "CREATE TABLE sesiones (" +
                            "sesion_id SERIAL PRIMARY KEY, " +
                            "usuario_id INTEGER REFERENCES usuarios(usuario_id) ON DELETE SET NULL, " +
                            "iniciada_en TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                            "finalizada_en TIMESTAMP, " +
                            "direccion_ip VARCHAR(45))"
            );
        }
    }

    private void insertarDatosPrueba(Connection conn) throws SQLException {
        // Insertar superadmin (credenciales: emilio/pass)
        String hashSuperadmin = BCrypt.hashpw("pass", BCrypt.gensalt());
        try (PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO usuarios (username, password_hash, rol) VALUES (?, ?, 'superadmin')")) {
            pstmt.setString(1, "emilio");
            pstmt.setString(2, hashSuperadmin);
            pstmt.executeUpdate();
        }

        // Insertar 2 admins
        String[] admins = {"admin1", "admin2"};
        for (String admin : admins) {
            String hash = BCrypt.hashpw("admin123", BCrypt.gensalt());
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO usuarios (username, password_hash, rol) VALUES (?, ?, 'admin')")) {
                pstmt.setString(1, admin);
                pstmt.setString(2, hash);
                pstmt.executeUpdate();
            }
        }

        // Insertar 4 usuarios normales
        for (int i = 1; i <= 4; i++) {
            String username = "usuario" + i;
            String hash = BCrypt.hashpw("user123", BCrypt.gensalt());
            try (PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO usuarios (username, password_hash, rol) VALUES (?, ?, 'usuario')")) {
                pstmt.setString(1, username);
                pstmt.setString(2, hash);
                pstmt.executeUpdate();
            }
        }

        // Insertar chats de prueba
        try (Statement stmt = conn.createStatement()) {
            // Chats para usuarios normales
            for (int i = 1; i <= 4; i++) {
                stmt.executeUpdate(
                        "INSERT INTO chats (usuario_id, titulo) VALUES " +
                                "(" + i + ", 'Chat de soporte de " + "usuario" + i + "')"
                );
            }

            // Insertar mensajes de prueba
            ResultSet rs = stmt.executeQuery("SELECT chat_id FROM chats");
            while (rs.next()) {
                int chatId = rs.getInt("chat_id");
                // Mensaje del usuario
                stmt.executeUpdate(
                        "INSERT INTO mensajes (chat_id, usuario_id, contenido) VALUES " +
                                "(" + chatId + ", " + ((chatId % 4) + 1) + ", 'Hola, tengo un problema con mi cuenta')"
                );

                // Respuesta del admin
                stmt.executeUpdate(
                        "INSERT INTO mensajes (chat_id, usuario_id, contenido) VALUES " +
                                "(" + chatId + ", 2, 'Hola, ¿en qué podemos ayudarte?')"
                );
            }
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpieza si es necesaria al detener la aplicación
    }
}