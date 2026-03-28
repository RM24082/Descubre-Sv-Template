package com.example.descubresv.controller;

import com.example.descubresv.dto.request.LoginRequest;
import com.example.descubresv.dto.request.RegistroRequest;
import com.example.descubresv.dto.response.ApiResponse;
import com.example.descubresv.dto.response.AuthResponse;
import com.example.descubresv.dto.response.UsuarioResponse;
import com.example.descubresv.security.JwtService;
import com.example.descubresv.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

// Controlador de autenticacion, maneja registro, login, logout y perfil
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticacion", description = "Registro e inicio de sesion de usuarios")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    // Registra un nuevo usuario y setea la cookie JWT
    @PostMapping("/registro")
    @Operation(summary = "Registrar usuario", description = "Crea una cuenta nueva y retorna el token en una cookie")
    public ResponseEntity<ApiResponse<AuthResponse>> registro(
            @Valid @RequestBody RegistroRequest request,
            HttpServletResponse response) {

        AuthResponse authResponse = authService.registrar(request);

        String token = authService.generarTokenPorCorreo(request.getCorreo());
        response.addCookie(jwtService.crearCookieJwt(token));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Usuario registrado exitosamente", authResponse));
    }

    // Valida credenciales y setea la cookie JWT
    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Valida credenciales y retorna el token en una cookie")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletResponse response) {

        AuthResponse authResponse = authService.login(request);

        String token = authService.generarTokenPorCorreo(request.getCorreo());
        response.addCookie(jwtService.crearCookieJwt(token));

        return ResponseEntity.ok(ApiResponse.success("Inicio de sesion exitoso", authResponse));
    }

    // Limpia la cookie JWT para cerrar sesion
    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesion", description = "Elimina la cookie JWT del navegador")
    public ResponseEntity<ApiResponse<String>> logout(HttpServletResponse response) {
        response.addCookie(jwtService.crearCookieCerrarSesion());
        return ResponseEntity.ok(ApiResponse.success("Sesion cerrada exitosamente", null));
    }

    // Retorna los datos del usuario autenticado
    @GetMapping("/perfil")
    @Operation(summary = "Obtener perfil", description = "Retorna los datos del usuario que tiene la sesion activa")
    public ResponseEntity<ApiResponse<UsuarioResponse>> perfil() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Long userId = (Long) ((UsernamePasswordAuthenticationToken) auth).getDetails();

        UsuarioResponse usuario = authService.obtenerPerfil(userId);
        return ResponseEntity.ok(ApiResponse.success(usuario));
    }
}
