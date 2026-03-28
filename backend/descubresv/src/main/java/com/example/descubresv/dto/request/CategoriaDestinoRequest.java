package com.example.descubresv.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos para crear o actualizar una categoria de destino
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDestinoRequest {

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
    private String nombreCategoria;

    private String descripcion;
}
