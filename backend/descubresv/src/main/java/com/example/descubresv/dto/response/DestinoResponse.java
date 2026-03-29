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

    // Identificador principal del destino en el sistema.
    private Long idDestino;

    // Datos base de presentacion del destino.
    private String nombre;
    private String descripcion;

    // Informacion de ubicacion administrativa.
    private String departamento;

    // Datos turisticos para planificacion del viaje.
    private BigDecimal precioEntrada;
    private String horario;
    private String mejorEpoca;
    private String tipo;

    // Indicaciones de acceso por distintos medios de transporte.
    private String comoLlegarVehiculo;
    private String comoLlegarBus;

    // Coordenadas geograficas para mapas y geolocalizacion.
    private BigDecimal latitud;
    private BigDecimal longitud;

    // Recursos visuales y calificacion del destino.
    private String imagenUrl;
    private BigDecimal calificacionPromedio;

    // Categoria relacionada enviada como subobjeto en la respuesta.
    private CategoriaDestinoResponse categoria;

    private Boolean activo;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Convierte entidad a DTO, incluye la categoria si existe
    public static DestinoResponse fromEntity(Destino destino) {
        // Inicializa la categoria en null para manejar destinos sin categoria asociada.
        CategoriaDestinoResponse categoriaDto = null;

        // Si existe categoria en la entidad, tambien se transforma a su DTO
        // correspondiente.
        if (destino.getCategoria() != null) {
            categoriaDto = CategoriaDestinoResponse.fromEntity(destino.getCategoria());
        }

        // Mapea todos los campos de la entidad al DTO de respuesta usando el patron
        // builder.
        return DestinoResponse.builder()
                .idDestino(destino.getIdDestino())
                .nombre(destino.getNombre())
                .descripcion(destino.getDescripcion())
                .departamento(destino.getDepartamento())
                .precioEntrada(destino.getPrecioEntrada())
                .horario(destino.getHorario())
                .mejorEpoca(destino.getMejorEpoca())
                .tipo(destino.getTipo())
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
