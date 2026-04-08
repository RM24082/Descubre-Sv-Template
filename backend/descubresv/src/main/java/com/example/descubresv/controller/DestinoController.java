package com.example.descubresv.controller;

import com.example.descubresv.dto.request.DestinoRequest;
import com.example.descubresv.dto.response.ApiResponse;
import com.example.descubresv.dto.response.DestinoResponse;
import com.example.descubresv.dto.response.PageResponse;
import com.example.descubresv.service.DestinoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// Controlador de destinos con rutas públicas de consulta y admin para gestion
@RestController
@Tag(name = "Destinos", description = "Consulta y gestion de destinos turisticos")
public class DestinoController {

    private final DestinoService destinoService;

    public DestinoController(DestinoService destinoService) {
        this.destinoService = destinoService;
    }

    // Lista destinos activos con filtros opcionales, acceso público
    @GetMapping("/api/destinos")
    @Operation(summary = "Listar destinos", description = "Lista destinos activos con filtros opcionales y paginacion")
    public ResponseEntity<ApiResponse<PageResponse<DestinoResponse>>> listar(
            @Parameter(description = "Filtrar por departamento") @RequestParam(required = false) String departamento,
            @Parameter(description = "Filtrar por tipo") @RequestParam(required = false) String tipo,
            @Parameter(description = "Filtrar por categoria") @RequestParam(required = false) Long categoria,
            @Parameter(description = "Buscar por nombre") @RequestParam(required = false) String buscar,
            Pageable pageable) {

        PageResponse<DestinoResponse> destinos;

        if (buscar != null && !buscar.isBlank()) {
            destinos = PageResponse.of(destinoService.buscarPorNombre(buscar, pageable));
        } else if (departamento != null && !departamento.isBlank()) {
            destinos = PageResponse.of(destinoService.listarPorDepartamento(departamento, pageable));
        } else if (tipo != null && !tipo.isBlank()) {
            destinos = PageResponse.of(destinoService.listarPorTipo(tipo, pageable));
        } else if (categoria != null) {
            destinos = PageResponse.of(destinoService.listarPorCategoria(categoria, pageable));
        } else {
            destinos = PageResponse.of(destinoService.listarActivos(pageable));
        }

        return ResponseEntity.ok(ApiResponse.success(destinos));
    }

    // Detalle de un destino por id, acceso publico
    @GetMapping("/api/destinos/{id}")
    @Operation(summary = "Obtener destino", description = "Obtiene el detalle completo de un destino por su id")
    public ResponseEntity<ApiResponse<DestinoResponse>> obtenerPorId(@PathVariable Long id) {
        DestinoResponse destino = destinoService.buscarPorId(id);
        return ResponseEntity.ok(ApiResponse.success(destino));
    }

    // Crea un nuevo destino, solo admin
    @PostMapping("/api/admin/destinos")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Crear destino", description = "Crea un nuevo destino turistico")
    public ResponseEntity<ApiResponse<DestinoResponse>> crear(
            @Valid @RequestBody DestinoRequest request) {
        DestinoResponse destino = destinoService.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Destino creado exitosamente", destino));
    }

    // Actualiza un destino existente, solo admin
    @PutMapping("/api/admin/destinos/{id}")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Actualizar destino", description = "Actualiza un destino existente")
    public ResponseEntity<ApiResponse<DestinoResponse>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody DestinoRequest request) {
        DestinoResponse destino = destinoService.actualizar(id, request);
        return ResponseEntity.ok(ApiResponse.success("Destino actualizado exitosamente", destino));
    }

    // Desactiva un destino, solo admin
    @DeleteMapping("/api/admin/destinos/{id}")
    @Tag(name = "Admin - Destinos")
    @Operation(summary = "Eliminar destino", description = "Desactiva un destino turistico")
    public ResponseEntity<ApiResponse<String>> eliminar(@PathVariable Long id) {
        destinoService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success("Destino eliminado exitosamente", null));
    }
}
