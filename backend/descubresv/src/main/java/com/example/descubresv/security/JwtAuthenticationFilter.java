package com.example.descubresv.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Filtro JWT - intercepta cada request para validar el token
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String token = extraerToken(request);

        // Si hay token y es valido, establecer autenticacion
        if (token != null && jwtService.validarToken(token)) {
            Long userId = jwtService.extraerUserId(token);
            String email = jwtService.extraerEmail(token);
            String rol = jwtService.extraerRol(token).name();

            // Crear autoridad con prefijo ROLE_ para Spring Security
            SimpleGrantedAuthority autoridad = new SimpleGrantedAuthority("ROLE_" + rol);

            // Crear token de autenticacion con el email como principal
            UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(email, null,
                    List.of(autoridad));

            // Guardar el userId como detalle adicional
            autenticacion.setDetails(userId);

            SecurityContextHolder.getContext().setAuthentication(autenticacion);
        }

        filterChain.doFilter(request, response);
    }

    // Extrae el JWT de la cookie o del header Authorization
    private String extraerToken(HttpServletRequest request) {
        // Primero intentar desde la cookie
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtService.getNombreCookie().equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }

        // Si no hay cookie, intentar desde el header Authorization
        String headerAuth = request.getHeader("Authorization");
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

    // No filtrar rutas publicas para mejor rendimiento
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String ruta = request.getRequestURI();
        return ruta.equals("/api/auth/registro")
                || ruta.equals("/api/auth/login")
                || ruta.equals("/api/auth/logout")
                || ruta.startsWith("/swagger-ui")
                || ruta.startsWith("/v3/api-docs")
                || ruta.equals("/");
    }
}
