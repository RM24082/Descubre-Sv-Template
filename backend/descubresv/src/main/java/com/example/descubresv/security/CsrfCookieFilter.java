package com.example.descubresv.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filtro CSRF Cookie - fuerza la generacion de la cookie XSRF-TOKEN en cada respuesta
// El frontend React leera esta cookie y la enviara como header X-XSRF-TOKEN
@SuppressWarnings("null")
public class CsrfCookieFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        // Obtener el token CSRF del request - esto fuerza su carga
        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
        if (csrfToken != null) {
            // Acceder al token para que Spring lo escriba en la cookie
            csrfToken.getToken();
        }
        filterChain.doFilter(request, response);
    }
}
