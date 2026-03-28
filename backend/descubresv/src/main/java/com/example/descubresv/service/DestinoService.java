package com.example.descubresv.service;

import com.example.descubresv.dto.request.DestinoRequest;
import com.example.descubresv.dto.response.DestinoResponse;
import com.example.descubresv.exception.ResourceNotFoundException;
import com.example.descubresv.model.entity.CategoriaDestino;
import com.example.descubresv.model.entity.Destino;
import com.example.descubresv.repository.CategoriaDestinoRepository;
import com.example.descubresv.repository.DestinoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

// Servicio para gestionar destinos turisticos con filtros y paginacion
@Service
public class DestinoService {

    private final DestinoRepository destinoRepository;
    private final CategoriaDestinoRepository categoriaRepository;

    public DestinoService(DestinoRepository destinoRepository,
                          CategoriaDestinoRepository categoriaRepository) {
        this.destinoRepository = destinoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // Lista destinos activos con paginacion
    public Page<DestinoResponse> listarActivos(Pageable pageable) {
        return destinoRepository.findByActivo(true, pageable)
                .map(DestinoResponse::fromEntity);
    }

    // Filtra destinos por departamento
    public Page<DestinoResponse> listarPorDepartamento(String departamento, Pageable pageable) {
        return destinoRepository.findByDepartamentoAndActivo(departamento, true, pageable)
                .map(DestinoResponse::fromEntity);
    }

    // Filtra destinos por tipo
    public Page<DestinoResponse> listarPorTipo(String tipo, Pageable pageable) {
        return destinoRepository.findByTipoAndActivo(tipo, true, pageable)
                .map(DestinoResponse::fromEntity);
    }

    // Filtra destinos por categoria
    public Page<DestinoResponse> listarPorCategoria(Long idCategoria, Pageable pageable) {
        return destinoRepository.findByCategoriaIdCategoriaAndActivo(idCategoria, true, pageable)
                .map(DestinoResponse::fromEntity);
    }

    // Busca destinos por nombre
    public Page<DestinoResponse> buscarPorNombre(String nombre, Pageable pageable) {
        return destinoRepository.findByNombreContainingIgnoreCaseAndActivo(nombre, true, pageable)
                .map(DestinoResponse::fromEntity);
    }

    // Busca un destino por su id
    public DestinoResponse buscarPorId(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destino no encontrado con id: " + id));
        return DestinoResponse.fromEntity(destino);
    }

    // Crea un nuevo destino, asigna la categoria si se proporciona idCategoria
    public DestinoResponse crear(DestinoRequest request) {
        CategoriaDestino categoria = null;
        if (request.getIdCategoria() != null) {
            categoria = categoriaRepository.findById(request.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoria no encontrada con id: " + request.getIdCategoria()));
        }

        Destino destino = Destino.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .departamento(request.getDepartamento())
                .precioEntrada(request.getPrecioEntrada())
                .horario(request.getHorario())
                .mejorEpoca(request.getMejorEpoca())
                .tipo(request.getTipo())
                .distanciaDesdeCapital(request.getDistanciaDesdeCapital())
                .comoLlegarVehiculo(request.getComoLlegarVehiculo())
                .comoLlegarBus(request.getComoLlegarBus())
                .latitud(request.getLatitud())
                .longitud(request.getLongitud())
                .imagenUrl(request.getImagenUrl())
                .categoria(categoria)
                .activo(true)
                .build();

        destino = destinoRepository.save(destino);
        return DestinoResponse.fromEntity(destino);
    }

    // Actualiza un destino existente con los datos del request
    public DestinoResponse actualizar(Long id, DestinoRequest request) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destino no encontrado con id: " + id));

        CategoriaDestino categoria = null;
        if (request.getIdCategoria() != null) {
            categoria = categoriaRepository.findById(request.getIdCategoria())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoria no encontrada con id: " + request.getIdCategoria()));
        }

        destino.setNombre(request.getNombre());
        destino.setDescripcion(request.getDescripcion());
        destino.setDepartamento(request.getDepartamento());
        destino.setPrecioEntrada(request.getPrecioEntrada());
        destino.setHorario(request.getHorario());
        destino.setMejorEpoca(request.getMejorEpoca());
        destino.setTipo(request.getTipo());
        destino.setDistanciaDesdeCapital(request.getDistanciaDesdeCapital());
        destino.setComoLlegarVehiculo(request.getComoLlegarVehiculo());
        destino.setComoLlegarBus(request.getComoLlegarBus());
        destino.setLatitud(request.getLatitud());
        destino.setLongitud(request.getLongitud());
        destino.setImagenUrl(request.getImagenUrl());
        destino.setCategoria(categoria);

        destino = destinoRepository.save(destino);
        return DestinoResponse.fromEntity(destino);
    }

    // Desactiva un destino en vez de eliminarlo
    public void eliminar(Long id) {
        Destino destino = destinoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Destino no encontrado con id: " + id));

        destino.setActivo(false);
        destinoRepository.save(destino);
    }
}
