package com.example.descubresv.repository;

import com.example.descubresv.model.entity.Favoritos;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// Repositorio de favoritos
@Repository
public interface FavoritoRepository extends JpaRepository<Favoritos, Long> {

    Page<Favoritos> findByUsuarioIdUsuario(Long idUsuario, Pageable pageable);

    Optional<Favoritos> findByUsuarioIdUsuarioAndDestinoIdDestino(Long idUsuario, Long idDestino);

    boolean existsByUsuarioIdUsuarioAndDestinoIdDestino(Long idUsuario, Long idDestino);
}
