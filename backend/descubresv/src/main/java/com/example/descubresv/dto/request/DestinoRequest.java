package com.example.descubresv.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Datos para crear o actualizar un destino turistico
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DestinoRequest {

    // Nombre principal del destino; obligatorio y con limite de longitud.
    @NotBlank(message = "El nombre del destino es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    // Descripcion general del atractivo, servicios o experiencia esperada.
    private String descripcion;

    // Departamento donde se ubica el destino; obligatorio para clasificacion
    // geografica.
    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
    private String departamento;

    // Costo estimado o tarifa de entrada al destino.
    private BigDecimal precioEntrada;

    // Horario de atencion o disponibilidad del destino.
    @Size(max = 200, message = "El horario no puede exceder 200 caracteres")
    private String horario;

    // Temporada recomendada para visitar el destino.
    @Size(max = 200, message = "La mejor epoca no puede exceder 200 caracteres")
    private String mejorEpoca;

    // Tipo de destino (ej. playa, montana, ciudad, cultural).
    @Size(max = 100, message = "El tipo no puede exceder 100 caracteres")
    private String tipo;

    // Indicaciones de llegada en vehiculo particular.
    private String comoLlegarVehiculo;

    // Indicaciones de llegada en transporte publico.
    private String comoLlegarBus;
    // Coordenada geograficas para ubicacion precisa del destino.
    private BigDecimal latitud;
    private BigDecimal longitud;

    // Enlace a imagen referencial del destino.
    @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    private String imagenUrl;

    // ID de la categoria, puede ser null si no se asigna
    private Long idCategoria;
}
