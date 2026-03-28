package com.example.descubresv.dto.response;

import com.example.descubresv.model.entity.Destino;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Datos del destino que se envian al cliente, incluye la categoria embebida
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinoResponse {

    private Long idDestino;
    private String nombre;
    private String descripcion;
    private String departamento;
    private BigDecimal precioEntrada;
    private String horario;
    private String mejorEpoca;
    private String tipo;
    private String distanciaDesdeCapital;
    private String comoLlegarVehiculo;
    private String comoLlegarBus;
    private BigDecimal latitud;
    private BigDecimal longitud;
    private String imagenUrl;
    private BigDecimal calificacionPromedio;
    private CategoriaDestinoResponse categoria;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Convierte entidad a DTO, incluye la categoria si existe
    public static DestinoResponse fromEntity(Destino destino) {
        CategoriaDestinoResponse categoriaDto = null;
        if (destino.getCategoria() != null) {
            categoriaDto = CategoriaDestinoResponse.fromEntity(destino.getCategoria());
        }

        return DestinoResponse.builder()
                .idDestino(destino.getIdDestino())
                .nombre(destino.getNombre())
                .descripcion(destino.getDescripcion())
                .departamento(destino.getDepartamento())
                .precioEntrada(destino.getPrecioEntrada())
                .horario(destino.getHorario())
                .mejorEpoca(destino.getMejorEpoca())
                .tipo(destino.getTipo())
                .distanciaDesdeCapital(destino.getDistanciaDesdeCapital())
                .comoLlegarVehiculo(destino.getComoLlegarVehiculo())
                .comoLlegarBus(destino.getComoLlegarBus())
                .latitud(destino.getLatitud())
                .longitud(destino.getLongitud())
                .imagenUrl(destino.getImagenUrl())
                .calificacionPromedio(destino.getCalificacionPromedio())
                .categoria(categoriaDto)
                .activo(destino.getActivo())
                .createdAt(destino.getCreatedAt())
                .updatedAt(destino.getUpdatedAt())
                .build();
    }
}
