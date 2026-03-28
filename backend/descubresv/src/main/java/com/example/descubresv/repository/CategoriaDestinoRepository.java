package com.example.descubresv.repository;

import com.example.descubresv.model.entity.CategoriaDestino;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio de categorias de destino
@Repository
public interface CategoriaDestinoRepository extends JpaRepository<CategoriaDestino, Long> {

    Page<CategoriaDestino> findByActivo(Boolean activo, Pageable pageable);
}
