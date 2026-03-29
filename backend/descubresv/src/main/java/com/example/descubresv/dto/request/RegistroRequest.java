package com.example.descubresv.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Datos necesarios para registrar un usuario nuevo
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistroRequest {

    // Nombre completo del usuario; se valida que no venga vacio y tenga longitud
    // razonable.
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    // Correo electronico de acceso; obligatorio y con formato valido.
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato del correo no es valido")
    private String correo;

    // Contrasena de autenticacion; obligatoria y con longitud minima de seguridad.
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // Dato opcional de perfil para personalizacion del usuario.
    @Size(max = 100, message = "La nacionalidad no puede exceder 100 caracteres")
    private String nacionalidad;
}
