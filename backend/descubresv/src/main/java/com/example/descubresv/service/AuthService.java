package com.example.descubresv.service;

import com.example.descubresv.dto.request.LoginRequest;
import com.example.descubresv.dto.request.RegistroRequest;
import com.example.descubresv.dto.response.AuthResponse;
import com.example.descubresv.dto.response.UsuarioResponse;
import com.example.descubresv.exception.EmailAlreadyExistsException;
import com.example.descubresv.exception.ResourceNotFoundException;
import com.example.descubresv.model.entity.Usuario;
import com.example.descubresv.model.enums.RolUsuario;
import com.example.descubresv.repository.UsuarioRepository;
import com.example.descubresv.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// Servicio de autenticacion, maneja registro, login y consulta de perfil
@Service
@SuppressWarnings("null")
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Registra un usuario nuevo, valida que el correo no exista y hashea la
    // contrasena
    public AuthResponse registrar(RegistroRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new EmailAlreadyExistsException("Ya existe un usuario con el correo: " + request.getCorreo());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .correo(request.getCorreo())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .nacionalidad(request.getNacionalidad())
                .rol(RolUsuario.TURISTA)
                .activo(true)
                .build();

        usuario = usuarioRepository.save(usuario);

        return AuthResponse.builder()
                .mensaje("Usuario registrado exitosamente")
                .usuario(UsuarioResponse.fromEntity(usuario))
                .build();
    }

    // Valida las credenciales del usuario y retorna el token en la respuesta
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        if (!usuario.getActivo()) {
            throw new BadCredentialsException("La cuenta esta desactivada");
        }

        return AuthResponse.builder()
                .mensaje("Inicio de sesion exitoso")
                .usuario(UsuarioResponse.fromEntity(usuario))
                .build();
    }

    // Genera el token JWT para un usuario autenticado
    public String generarToken(Usuario usuario) {
        return jwtService.generarToken(
                usuario.getIdUsuario(),
                usuario.getCorreo(),
                usuario.getRol());
    }

    // Genera el token JWT usando el correo para buscarlo
    public String generarTokenPorCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
        return generarToken(usuario);
    }

    // Obtiene el perfil del usuario autenticado por su id
    public UsuarioResponse obtenerPerfil(Long userId) {
        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + userId));

        return UsuarioResponse.fromEntity(usuario);
    }
}
