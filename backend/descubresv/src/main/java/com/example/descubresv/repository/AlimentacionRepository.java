package com.example.descubresv.repository;

import com.example.descubresv.model.entity.Alimentacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repositorio de alimentacion
@Repository
public interface AlimentacionRepository extends JpaRepository<Alimentacion, Long> {

    Page<Alimentacion> findByDestinoIdDestinoAndActivo(Long idDestino, Boolean activo, Pageable pageable);
}
