package com.example.descubresv.repository;

import com.example.descubresv.model.entity.Transporte;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio de transportes
@Repository
public interface TransporteRepository extends JpaRepository<Transporte, Long> {

    Page<Transporte> findByDestinoIdDestinoAndActivo(Long idDestino, Boolean activo, Pageable pageable);
}
