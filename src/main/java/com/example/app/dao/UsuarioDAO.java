// src/main/java/com/example/app/dao/UsuarioDAO.java
package com.example.app.dao;

import com.example.app.models.Usuario;
import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class UsuarioDAO {
    public Usuario autenticar(String username, String password) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE username = ?";
        System.out.println("Ejecutando query: " + sql); // Debug

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Usuario encontrado: " + username); // Debug
                String hashBD = rs.getString("password_hash");
                System.out.println("Hash en BD: " + hashBD); // Debug

                if (BCrypt.checkpw(password, hashBD)) {
                    System.out.println("Contraseña válida"); // Debug
                    return mapearUsuario(rs);
                } else {
                    System.out.println("Contraseña inválida"); // Debug
                }
            } else {
                System.out.println("Usuario no encontrado"); // Debug
            }
            return null;
        }
    }

    public void insertar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (username, password_hash, rol, creado_en) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection(); // Obtener la conexión aquí
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getUsername());
            pstmt.setString(2, usuario.getPasswordHash());
            pstmt.setString(3, usuario.getRol());
            pstmt.setObject(4, usuario.getCreadoEn());
            pstmt.executeUpdate();
        } // La conexión y el PreparedStatement se cierran automáticamente aquí
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setUsuarioId(rs.getInt("usuario_id"));
        usuario.setUsername(rs.getString("username"));
        usuario.setRol(rs.getString("rol"));
        System.out.println("[UsuarioDAO] rol : "+ usuario.getRol());
        // ... otros campos
        return usuario;
    }

    public List<Usuario> obtenerTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY creado_en DESC";

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(mapearUsuario(rs));
            }
        }

        return usuarios;
    }

    public Usuario obtenerPorId(int usuarioId) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE usuario_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapearUsuario(rs);
            }
            return null;
        }
    }

    public void actualizarRol(int usuarioId, String nuevoRol) throws SQLException {
        String sql = "UPDATE usuarios SET rol = ? WHERE usuario_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoRol);
            stmt.setInt(2, usuarioId);
            stmt.executeUpdate();
        }
    }

}