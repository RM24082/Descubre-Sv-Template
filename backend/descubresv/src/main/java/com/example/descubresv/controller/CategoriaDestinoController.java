package com.example.descubresv.controller;

import com.example.descubresv.dto.request.CategoriaDestinoRequest;
import com.example.descubresv.dto.response.ApiResponse;
import com.example.descubresv.dto.response.CategoriaDestinoResponse;
import com.example.descubresv.dto.response.PageResponse;
import com.example.descubresv.service.CategoriaDestinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador de categorias con rutas publicas y de admin
@RestController
@Tag(name = "Categorias", description = "Categorias de destinos turisticos")
public class CategoriaDestinoController {

    private final CategoriaDestinoService categoriaService;

    public CategoriaDestinoController(CategoriaDestinoService categoriaService) {
        this.categoriaService = categoriaService;
    }

    // Lista categorias activas paginadas, acceso publico
    @GetMapping("/api/categorias")
    @Operation(summary = "Listar categorias", description = "Lista todas las categorias activas con paginacion")
    public ResponseEntity<ApiResponse<PageResponse<CategoriaDestinoResponse>>> listar(Pageable pageable) {
        PageResponse<CategoriaDestinoResponse> categorias = PageResponse.of(categoriaService.listarActivas(pageable));
        return ResponseEntity.ok(ApiResponse.success(categorias));
    }

    // Lista todas las categorias activas sin paginar, util para selects
    @GetMapping("/api/categorias/todas")
    @Operation(summary = "Listar todas las categorias", description = "Lista todas las categorias activas sin paginacion")
    public ResponseEntity<ApiResponse<List<CategoriaDestinoResponse>>> listarTodas() {
        List<CategoriaDestinoResponse> categorias = categoriaService.listarTodasActivas();
        return ResponseEntity.ok(ApiResponse.success(categorias));
    }

    // Detalle de una categoria por id, acceso publico
    @GetMapping("/api/categorias/{id}")
    @Operation(summary = "Obtener categoria", description = "Obtiene el detalle de una categoria por su id")
    public ResponseEntity<ApiResponse<CategoriaDestinoResponse>> obtenerPorId(@PathVariable Long id) {
        CategoriaDestinoResponse categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(categoria));
    }

    // Crea una nueva categoria, solo admin
    @PostMapping("/api/admin/categorias")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Crear categoria", description = "Crea una nueva categoria de destino")
    public ResponseEntity<ApiResponse<CategoriaDestinoResponse>> crear(
            @Valid @RequestBody CategoriaDestinoRequest request) {
        CategoriaDestinoResponse categoria = categoriaService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Categoria creada exitosamente", categoria));
    }

    // Actualiza una categoria existente, solo admin
    @PutMapping("/api/admin/categorias/{id}")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Actualizar categoria", description = "Actualiza una categoria existente")
    public ResponseEntity<ApiResponse<CategoriaDestinoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDestinoRequest request) {
        CategoriaDestinoResponse categoria = categoriaService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success("Categoria actualizada exitosamente", categoria));
    }

    // Desactiva una categoria, solo admin
    @DeleteMapping("/api/admin/categorias/{id}")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Eliminar categoria", description = "Desactiva una categoria de destino")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Categoria eliminada exitosamente", null));
    }
}
