package com.example.app.models;

import java.time.LocalDateTime;

public class Mensaje {
    private int mensajeId;
    private int chatId;
    private Integer usuarioId;
    private String contenido;
    private LocalDateTime enviadoEn;
    private boolean editado;

    // Constructor vacío
    public Mensaje() {}

    // Constructor con campos
    public Mensaje(int mensajeId, int chatId, Integer usuarioId, String contenido,
                   LocalDateTime enviadoEn, boolean editado) {
        this.mensajeId = mensajeId;
        this.chatId = chatId;
        this.usuarioId = usuarioId;
        this.contenido = contenido;
        this.enviadoEn = enviadoEn;
        this.editado = editado;
    }

    // Getters y Setters
    public int getMensajeId() {
        return mensajeId;
    }

    public void setMensajeId(int mensajeId) {
        this.mensajeId = mensajeId;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDateTime getEnviadoEn() {
        return enviadoEn;
    }

    public void setEnviadoEn(LocalDateTime enviadoEn) {
        this.enviadoEn = enviadoEn;
    }

    public boolean isEditado() {
        return editado;
    }

    public void setEditado(boolean editado) {
        this.editado = editado;
    }
}