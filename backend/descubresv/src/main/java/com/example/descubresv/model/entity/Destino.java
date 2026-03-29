package com.example.descubresv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entidad destino - Información de destinos turísticos
@Entity
@Table(name = "destinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destino {

    // Identificador unico autogenerado de cada destino turistico
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_destino")
    private Long idDestino;

    // Nombre oficial o denominacion comercial del destino
    @Column(nullable = false, length = 200)
    private String nombre;

    // Descripcion detallada de caracteristicas, atractivos y servicios disponibles
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    // Ubicacion geografica administrativa: departamento o region del pais
    @Column(nullable = false, length = 100)
    private String departamento;

    // Costo de entrada o tarifa de acceso al destino
    @Column(name = "precio_entrada", precision = 10, scale = 2)
    private BigDecimal precioEntrada;

    // Horario de atencion u operacion
    @Column(length = 200)
    private String horario;

    // Periodo o estacion optima para visitar (ej. verano, invierno, todo el ano)
    @Column(name = "mejor_epoca", length = 200)
    private String mejorEpoca;

    // Clasificacion del destino: playa, montana, ciudad, arqueologico, etc
    @Column(length = 100)
    private String tipo;

    // Instrucciones detalladas para llegar al destino en vehiculo particular
    @Column(name = "como_llegar_vehiculo", columnDefinition = "TEXT")
    private String comoLlegarVehiculo;

    // Instrucciones detalladas para llegar al destino en transporte publico
    @Column(name = "como_llegar_bus", columnDefinition = "TEXT")
    private String comoLlegarBus;

    // Coordenada geografica norte-sur para ubicacion precisa en mapas
    @Column(precision = 10, scale = 7)
    private BigDecimal latitud;

    @Column(precision = 10, scale = 7)
    private BigDecimal longitud;

    // URL o ruta a imagen representativa del destino para visualizacion
    @Column(name = "imagen_url", length = 500)
    private String imagenUrl;

    // Promedio de puntuaciones otorgadas por usuarios del sistema
    @Column(name = "calificacion_promedio", precision = 3, scale = 2)
    private BigDecimal calificacionPromedio;

    // Relacion muchos-a-uno: un destino pertenece a una sola categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria")
    private CategoriaDestino categoria;

    // Bandera de estado logico para habilitar/deshabilitar destino sin eliminar
    // historico
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
