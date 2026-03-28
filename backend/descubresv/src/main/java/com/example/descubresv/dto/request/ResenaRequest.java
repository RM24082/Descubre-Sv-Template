package com.example.descubresv.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos para crear una resena sobre un destino
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResenaRequest {

    @NotNull(message = "El id del destino es obligatorio")
    private Long idDestino;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    private String comentario;
}
