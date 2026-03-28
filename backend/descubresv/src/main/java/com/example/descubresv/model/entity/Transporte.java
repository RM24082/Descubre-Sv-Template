package com.example.descubresv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entidad transporte - opciones de transporte por destino
@Entity
@Table(name = "transportes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transporte {

    // Identificador unico autogenerado de cada opcion de transporte.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transporte")
    private Long idTransporte;

    // Tipo de transporte ofrecido
    // Transporte público
    @Column(nullable = false, length = 100)
    private String tipo;

    // Costo estimado del transporte para llegar o movilizarse.
    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    // Tiempo aproximado de traslado usando este medio de transporte.
    @Column(name = "tiempo_estimado", length = 100)
    private String tiempoEstimado;

    // Relacion muchos-a-uno: cada opcion de transporte se asocia a un destino.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", nullable = false)
    private Destino destino;

    // Bandera de estado logico para activar/desactivar sin borrar historico.
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // Fecha y hora de creacion del registro, asignada automaticamente.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Fecha y hora de ultima actualizacion del registro.
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
