package com.example.descubresv.config;

import com.example.descubresv.security.CsrfCookieFilter;
import com.example.descubresv.security.JwtAuthenticationFilter;
import com.example.descubresv.security.SpaCsrfTokenRequestHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

// Configuracion principal de seguridad - JWT, CSRF, CORS y control de acceso por roles
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        @Value("${app.cors.allowed-origins}")
        private String[] origenesPermitidos;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
                this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        // Cadena de filtros de seguridad principal
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // Configuracion CORS - permite requests desde React
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // Configuracion CSRF con cookies para SPA React
                                .csrf(csrf -> csrf
                                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                                .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
                                                // Ignorar CSRF en endpoints de autenticacion y Swagger
                                                .ignoringRequestMatchers(
                                                                "/api/auth/**",
                                                                "/swagger-ui/**",
                                                                "/v3/api-docs/**"))

                                // Sesiones stateless - usamos JWT, no sesiones del servidor
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // Reglas de acceso a endpoints
                                .authorizeHttpRequests(auth -> auth
                                                // Rutas publicas de auth, perfil queda protegido
                                                .requestMatchers("/api/auth/registro", "/api/auth/login",
                                                                "/api/auth/logout")
                                                .permitAll()
                                                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                                                .requestMatchers("/v3/api-docs/**").permitAll()
                                                .requestMatchers("/api/public/**").permitAll()

                                                // Rutas de consulta publica - GET sin autenticacion para destinos
                                                .requestMatchers(HttpMethod.GET, "/api/destinos/**").permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()

                                                // Rutas exclusivas de administrador
                                                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                                                // Rutas exclusivas de turista
                                                .requestMatchers("/api/turista/**").hasRole("TURISTA")

                                                .anyRequest().authenticated())

                                // Headers de seguridad adicionales
                                .headers(headers -> headers
                                                // Prevenir clickjacking
                                                .frameOptions(frame -> frame.deny())
                                                // Prevenir MIME type sniffing
                                                .contentTypeOptions(content -> {
                                                }))

                                // Agregar filtro CSRF cookie antes del filtro JWT
                                .addFilterAfter(new CsrfCookieFilter(), UsernamePasswordAuthenticationFilter.class)

                                // Agregar filtro JWT antes del filtro de autenticacion de Spring
                                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // Configuracion CORS - permite el frontend React enviar cookies
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuracion = new CorsConfiguration();
                configuracion.setAllowedOrigins(Arrays.asList(origenesPermitidos));
                configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuracion.setAllowedHeaders(List.of("*"));
                // Permitir el envio de cookies y credenciales
                configuracion.setAllowCredentials(true);
                // Exponer headers personalizados al frontend
                configuracion.setExposedHeaders(List.of("X-XSRF-TOKEN"));
                configuracion.setMaxAge(3600L);

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuracion);
                return source;
        }

        // EncriptAcion BCrypt para contraseñas
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        // Authentication Manager para uso en el servicio de autenticacion
        @Bean
        public AuthenticationManager authenticationManager(
                        AuthenticationConfiguration authConfig) throws Exception {
                return authConfig.getAuthenticationManager();
        }
}
