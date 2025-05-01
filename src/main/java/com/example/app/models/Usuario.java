package com.example.app.models;

import java.time.LocalDateTime;

public class Usuario {
    private int usuarioId;
    private String username;
    private String passwordHash;
    private String rol;
    private LocalDateTime creadoEn;
    private LocalDateTime ultimoLogin;

    // Constructor vacío
    public Usuario() {}

    // Constructor con campos
    public Usuario(int usuarioId, String username, String passwordHash, String rol,
                   LocalDateTime creadoEn, LocalDateTime ultimoLogin) {
        this.usuarioId = usuarioId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
        this.creadoEn = creadoEn;
        this.ultimoLogin = ultimoLogin;
    }

    // Getters y Setters
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDateTime getCreadoEn() {
        return creadoEn;
    }

    public void setCreadoEn(LocalDateTime creadoEn) {
        this.creadoEn = creadoEn;
    }

    public LocalDateTime getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(LocalDateTime ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }
}