// src/main/java/com/example/app/dao/MensajeDAO.java
package com.example.app.dao;


import com.example.app.models.Mensaje;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class MensajeDAO {


    public List<Mensaje> listarMensajesPorChat(int chatId) {
        String sql = "SELECT * FROM mensajes WHERE chat_id = ? ORDER BY enviado_en";
        List<Mensaje> mensajes = new ArrayList<>();
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    mensajes.add(mapearMensaje(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los mensajes del chat: " + e.getMessage());
            e.printStackTrace();
        }
        return mensajes;
    }


    public void insertar(Mensaje mensaje) {
        String sql = "INSERT INTO mensajes (chat_id, usuario_id, contenido, enviado_en, editado) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, mensaje.getChatId());
            pstmt.setObject(2, mensaje.getUsuarioId(), Types.INTEGER); // Manejar el Integer nullable
            pstmt.setString(3, mensaje.getContenido());
            pstmt.setObject(4, mensaje.getEnviadoEn());
            pstmt.setBoolean(5, mensaje.isEditado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar el mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Mensaje mapearMensaje(ResultSet rs) throws SQLException {
        Mensaje mensaje = new Mensaje();
        mensaje.setMensajeId(rs.getInt("mensaje_id"));
        mensaje.setChatId(rs.getInt("chat_id"));
        mensaje.setUsuarioId((Integer) rs.getObject("usuario_id")); // Manejar el Integer nullable
        mensaje.setContenido(rs.getString("contenido"));
        mensaje.setEnviadoEn(rs.getObject("enviado_en", LocalDateTime.class));
        mensaje.setEditado(rs.getBoolean("editado"));
        return mensaje;
    }
}