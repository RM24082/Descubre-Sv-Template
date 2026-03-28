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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transporte")
    private Long idTransporte;

    @Column(nullable = false, length = 100)
    private String tipo;

    @Column(precision = 10, scale = 2)
    private BigDecimal costo;

    @Column
    private Integer capacidad;

    @Column(name = "tiempo_estimado", length = 100)
    private String tiempoEstimado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", nullable = false)
    private Destino destino;

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
