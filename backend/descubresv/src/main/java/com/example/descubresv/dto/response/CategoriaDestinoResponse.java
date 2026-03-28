package com.example.descubresv.dto.response;

import com.example.descubresv.model.entity.CategoriaDestino;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Datos de categoria que se envian al cliente
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaDestinoResponse {

    private Long idCategoria;
    private String nombreCategoria;
    private String descripcion;
    private Boolean activo;
    private LocalDateTime createdAt;

    // Convierte entidad a DTO
    public static CategoriaDestinoResponse fromEntity(CategoriaDestino categoria) {
        return CategoriaDestinoResponse.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombreCategoria(categoria.getNombreCategoria())
                .descripcion(categoria.getDescripcion())
                .activo(categoria.getActivo())
                .createdAt(categoria.getCreatedAt())
                .build();
    }
}
