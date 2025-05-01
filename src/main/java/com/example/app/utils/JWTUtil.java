// src/main/java/com/example/app/utils/JWTUtil.java
package com.example.app.utils;

import com.example.app.models.Usuario;
import io.jsonwebtoken.*;
import java.util.Date;

public class JWTUtil {
    private static final String SECRET = "tu_super_secreto";
    private static final long EXPIRATION = 86400000; // 24h

    public static String generarToken(Usuario usuario) {
        return Jwts.builder()
                .setSubject(usuario.getUsername())
                .claim("rol", usuario.getRol())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    public static boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}