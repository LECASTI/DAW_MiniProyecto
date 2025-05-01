// src/main/java/com/example/app/dao/ChatDAO.java
package com.example.app.dao;


import com.example.app.models.Chat;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ChatDAO {


    public Chat obtenerChatPorUsuario(int usuarioId) {
        String sql = "SELECT * FROM chats WHERE usuario_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuarioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearChat(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener el chat del usuario: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public void insertar(Chat chat) {
        String sql = "INSERT INTO chats (usuario_id, titulo, estado, creado_en) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chat.getUsuarioId());
            pstmt.setString(2, chat.getTitulo());
            pstmt.setString(3, chat.getEstado());
            pstmt.setObject(4, chat.getCreadoEn());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar el chat: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void eliminar(int chatId) {
        String sql = "DELETE FROM chats WHERE chat_id = ?";
        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, chatId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar el chat: " + e.getMessage());
            e.printStackTrace();
        }
    }


    private Chat mapearChat(ResultSet rs) throws SQLException {
        Chat chat = new Chat();
        chat.setChatId(rs.getInt("chat_id"));
        chat.setUsuarioId(rs.getInt("usuario_id"));
        chat.setTitulo(rs.getString("titulo"));
        chat.setEstado(rs.getString("estado"));
        chat.setCreadoEn(rs.getObject("creado_en", LocalDateTime.class));
        return chat;
    }
}