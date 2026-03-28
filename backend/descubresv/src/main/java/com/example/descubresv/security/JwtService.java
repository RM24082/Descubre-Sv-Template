package com.example.descubresv.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.example.descubresv.model.enums.RolUsuario;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

// Servicio JWT - generacion, validacion y extraccion de datos del token
@Service
public class JwtService {

    private final SecretKey claveSecreta;
    private final long tiempoExpiracion;
    private final String nombreCookie;

    // Inyeccion de valores desde application.properties
    public JwtService(
            @Value("${jwt.secret}") String secreto,
            @Value("${jwt.expiration}") long expiracion,
            @Value("${jwt.cookie-name}") String cookie) {
        this.claveSecreta = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secreto));
        this.tiempoExpiracion = expiracion;
        this.nombreCookie = cookie;
    }

    // Genera un token JWT con los datos del usuario
    public String generarToken(Long userId, String email, RolUsuario rol) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + tiempoExpiracion);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .claim("rol", rol.name())
                .issuedAt(ahora)
                .expiration(expiracion)
                .signWith(claveSecreta)
                .compact();
    }

    // Valida si el token es correcto y no ha expirado
    public boolean validarToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(claveSecreta)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Extrae todos los claims del token
    public Claims extraerClaims(String token) {
        return Jwts.parser()
                .verifyWith(claveSecreta)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Extrae el ID del usuario desde el token
    public Long extraerUserId(String token) {
        return Long.parseLong(extraerClaims(token).getSubject());
    }

    // Extrae el email desde el token
    public String extraerEmail(String token) {
        return extraerClaims(token).get("email", String.class);
    }

    // Extrae el rol del usuario desde el token
    public RolUsuario extraerRol(String token) {
        String rol = extraerClaims(token).get("rol", String.class);
        return RolUsuario.valueOf(rol);
    }

    // Crea una cookie HttpOnly con el JWT para proteger contra XSS
    public Cookie crearCookieJwt(String token) {
        Cookie cookie = new Cookie(nombreCookie, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge((int) (tiempoExpiracion / 1000));
        return cookie;
    }

    // Crea una cookie vacia para cerrar sesion
    public Cookie crearCookieCerrarSesion() {
        Cookie cookie = new Cookie(nombreCookie, "");
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    // Retorna el nombre de la cookie JWT
    public String getNombreCookie() {
        return nombreCookie;
    }
}
