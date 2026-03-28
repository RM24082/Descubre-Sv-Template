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

    @NotBlank(message = "El nombre del destino es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    private String descripcion;

    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 100, message = "El departamento no puede exceder 100 caracteres")
    private String departamento;

    private BigDecimal precioEntrada;

    @Size(max = 200, message = "El horario no puede exceder 200 caracteres")
    private String horario;

    @Size(max = 200, message = "La mejor epoca no puede exceder 200 caracteres")
    private String mejorEpoca;

    @Size(max = 100, message = "El tipo no puede exceder 100 caracteres")
    private String tipo;

    @Size(max = 100)
    private String distanciaDesdeCapital;

    private String comoLlegarVehiculo;

    private String comoLlegarBus;

    private BigDecimal latitud;

    private BigDecimal longitud;

    @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    private String imagenUrl;

    // ID de la categoria, puede ser null si no se asigna
    private Long idCategoria;
}
