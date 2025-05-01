package com.example.app.models;

import java.time.LocalDateTime;

public class Chat {
    private int chatId;
    private int usuarioId;
    private String titulo;
    private String estado;
    private LocalDateTime creadoEn;

    // Constructor vacío
    public Chat() {}

    // Constructor con campos
    public Chat(int chatId, int usuarioId, String titulo, String estado, LocalDateTime creadoEn) {
        this.chatId = chatId;
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.estado = estado;
        this.creadoEn = creadoEn;
    }

    // Getters y Setters
    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }
}