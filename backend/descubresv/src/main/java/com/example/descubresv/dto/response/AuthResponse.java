package com.example.descubresv.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Respuesta de autenticacion con mensaje y datos del usuario
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    private String mensaje;
    private UsuarioResponse usuario;
}
