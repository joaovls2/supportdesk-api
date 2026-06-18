package com.supportdesk.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private static final String SECRET_KEY =
            "supportdesk-secret-key-para-jwt-com-pelo-menos-32-caracteres";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 2;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String gerarToken(
            String email,
            String tipo,
            Long empresaId) {

        return Jwts.builder()
                .subject(email)
                .claim("tipo", tipo)
                .claim("empresaId", empresaId)
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + EXPIRATION_TIME)
                )
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extrairClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extrairEmail(String token) {
        return extrairClaims(token).getSubject();
    }

    public String extrairTipo(String token) {
        return extrairClaims(token).get("tipo", String.class);
    }

    public Long extrairEmpresaId(String token) {
        return extrairClaims(token).get("empresaId", Long.class);
    }

    public boolean tokenValido(String token) {
        try {
            extrairClaims(token);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}