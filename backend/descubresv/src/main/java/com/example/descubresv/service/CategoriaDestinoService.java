package com.example.descubresv.service;

import com.example.descubresv.dto.request.CategoriaDestinoRequest;
import com.example.descubresv.dto.response.CategoriaDestinoResponse;
import com.example.descubresv.exception.ResourceNotFoundException;
import com.example.descubresv.model.entity.CategoriaDestino;
import com.example.descubresv.repository.CategoriaDestinoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Servicio para gestionar las categorias de destinos turisticos
@Service
public class CategoriaDestinoService {

    private final CategoriaDestinoRepository categoriaRepository;

    public CategoriaDestinoService(CategoriaDestinoRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    // Lista todas las categorias activas con paginacion
    public Page<CategoriaDestinoResponse> listarActivas(Pageable pageable) {
        return categoriaRepository.findByActivo(true, pageable)
                .map(CategoriaDestinoResponse::fromEntity);
    }

    // Lista todas las categorias activas sin paginacion
    public List<CategoriaDestinoResponse> listarTodasActivas() {
        return categoriaRepository.findAll().stream()
                .filter(CategoriaDestino::getActivo)
                .map(CategoriaDestinoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Busca una categoria por su id
    public CategoriaDestinoResponse buscarPorId(Long id) {
        CategoriaDestino categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));
        return CategoriaDestinoResponse.fromEntity(categoria);
    }

    // Crea una nueva categoria
    public CategoriaDestinoResponse crear(CategoriaDestinoRequest request) {
        CategoriaDestino categoria = CategoriaDestino.builder()
                .nombreCategoria(request.getNombreCategoria())
                .descripcion(request.getDescripcion())
                .activo(true)
                .build();

        categoria = categoriaRepository.save(categoria);
        return CategoriaDestinoResponse.fromEntity(categoria);
    }

    // Actualiza una categoria existente
    public CategoriaDestinoResponse actualizar(Long id, CategoriaDestinoRequest request) {
        CategoriaDestino categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        categoria.setNombreCategoria(request.getNombreCategoria());
        categoria.setDescripcion(request.getDescripcion());

        categoria = categoriaRepository.save(categoria);
        return CategoriaDestinoResponse.fromEntity(categoria);
    }

    // Desactiva una categoria en vez de eliminarla
    public void eliminar(Long id) {
        CategoriaDestino categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria no encontrada con id: " + id));

        categoria.setActivo(false);
        categoriaRepository.save(categoria);
    }
}
