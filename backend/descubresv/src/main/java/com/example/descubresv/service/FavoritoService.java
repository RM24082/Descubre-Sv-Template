package com.example.descubresv.service;

import com.example.descubresv.dto.response.FavoritoResponse;
import com.example.descubresv.exception.ResourceNotFoundException;
import com.example.descubresv.model.entity.Destino;
import com.example.descubresv.model.entity.Favoritos;
import com.example.descubresv.model.entity.Usuario;
import com.example.descubresv.repository.DestinoRepository;
import com.example.descubresv.repository.FavoritoRepository;
import com.example.descubresv.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

// Servicio para gestionar favoritos del turista
@Service
public class FavoritoService {

    private final FavoritoRepository favoritoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DestinoRepository destinoRepository;

    public FavoritoService(FavoritoRepository favoritoRepository,
                           UsuarioRepository usuarioRepository,
                           DestinoRepository destinoRepository) {
        this.favoritoRepository = favoritoRepository;
        this.usuarioRepository = usuarioRepository;
        this.destinoRepository = destinoRepository;
    }

    // Agrega o quita un destino de favoritos, funciona como toggle
    public boolean toggle(Long userId, Long idDestino) {
        Optional<Favoritos> existente = favoritoRepository
                .findByUsuarioIdUsuarioAndDestinoIdDestino(userId, idDestino);

        if (existente.isPresent()) {
            favoritoRepository.delete(existente.get());
            return false;
        }

        Usuario usuario = usuarioRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        Destino destino = destinoRepository.findById(idDestino)
                .orElseThrow(() -> new ResourceNotFoundException("Destino no encontrado con id: " + idDestino));

        Favoritos favorito = Favoritos.builder()
                .usuario(usuario)
                .destino(destino)
                .build();

        favoritoRepository.save(favorito);
        return true;
    }

    // Lista los destinos favoritos del usuario autenticado
    public Page<FavoritoResponse> listarMisFavoritos(Long userId, Pageable pageable) {
        return favoritoRepository.findByUsuarioIdUsuario(userId, pageable)
                .map(FavoritoResponse::fromEntity);
    }

    // Verifica si un destino esta en los favoritos del usuario
    public boolean esFavorito(Long userId, Long idDestino) {
        return favoritoRepository.existsByUsuarioIdUsuarioAndDestinoIdDestino(userId, idDestino);
    }
}
