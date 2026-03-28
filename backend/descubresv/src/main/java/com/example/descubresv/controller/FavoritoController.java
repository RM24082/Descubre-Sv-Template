package com.example.descubresv.controller;

import com.example.descubresv.dto.response.ApiResponse;
import com.example.descubresv.dto.response.FavoritoResponse;
import com.example.descubresv.dto.response.PageResponse;
import com.example.descubresv.service.FavoritoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Controlador de favoritos para turistas autenticados
@RestController
@RequestMapping("/api/turista/favoritos")
@Tag(name = "Favoritos", description = "Gestion de destinos favoritos del turista")
public class FavoritoController {

    private final FavoritoService favoritoService;

    public FavoritoController(FavoritoService favoritoService) {
        this.favoritoService = favoritoService;
    }

    // Agrega o quita un destino de favoritos
    @PostMapping("/{idDestino}")
    @Operation(summary = "Toggle favorito", description = "Agrega o quita un destino de tus favoritos")
    public ResponseEntity<ApiResponse<Map<String, Object>>> toggle(@PathVariable Long idDestino) {
        Long userId = obtenerUserId();
        boolean agregado = favoritoService.toggle(userId, idDestino);

        String mensaje = agregado ? "Destino agregado a favoritos" : "Destino eliminado de favoritos";
        Map<String, Object> data = Map.of("esFavorito", agregado);

        return ResponseEntity.ok(ApiResponse.success(mensaje, data));
    }

    // Lista los destinos favoritos del turista autenticado
    @GetMapping
    @Operation(summary = "Mis favoritos", description = "Lista los destinos favoritos del turista autenticado")
    public ResponseEntity<ApiResponse<PageResponse<FavoritoResponse>>> misFavoritos(Pageable pageable) {
        Long userId = obtenerUserId();
        PageResponse<FavoritoResponse> favoritos = PageResponse.of(favoritoService.listarMisFavoritos(userId, pageable));
        return ResponseEntity.ok(ApiResponse.success(favoritos));
    }

    // Verifica si un destino es favorito del turista
    @GetMapping("/{idDestino}/estado")
    @Operation(summary = "Verificar favorito", description = "Verifica si un destino esta en tus favoritos")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> verificar(@PathVariable Long idDestino) {
        Long userId = obtenerUserId();
        boolean esFavorito = favoritoService.esFavorito(userId, idDestino);
        return ResponseEntity.ok(ApiResponse.success(Map.of("esFavorito", esFavorito)));
    }

    // Extrae el userId del contexto de seguridad
    private Long obtenerUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (Long) ((UsernamePasswordAuthenticationToken) auth).getDetails();
    }
}
