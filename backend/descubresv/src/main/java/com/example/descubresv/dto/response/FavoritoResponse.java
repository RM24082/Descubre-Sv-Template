package com.example.descubresv.dto.response;

import com.example.descubresv.model.entity.Favorito;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Datos del favorito con el destino embebido para mostrar en la lista
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FavoritoResponse {

    private Long idFavorito;
    private DestinoResponse destino;
    private LocalDateTime createdAt;

    // Convierte entidad a DTO, incluye el destino completo
    public static FavoritoResponse fromEntity(Favorito favorito) {
        return FavoritoResponse.builder()
                .idFavorito(favorito.getIdFavorito())
                .destino(DestinoResponse.fromEntity(favorito.getDestino()))
                .createdAt(favorito.getCreatedAt())
                .build();
    }
}
