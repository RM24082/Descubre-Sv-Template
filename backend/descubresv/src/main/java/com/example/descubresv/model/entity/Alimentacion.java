package com.example.descubresv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Entidad alimentacion - Opciones de comida por destino
@Entity
@Table(name = "alimentacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Alimentacion {

    // Identificador primario autogenerado de cada registro de alimentacion.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alimentacion")
    private Long idAlimentacion;

    // Nombre comercial o referencia principal del lugar/comida.
    @Column(nullable = false, length = 200)
    private String nombre;

    // Categoria del tipo de comida (ej. tipica, rapida, gourmet).
    @Column(name = "tipo_comida", length = 100)
    private String tipoComida;

    // Valor estimado del costo promedio de consumo.
    @Column(name = "precio_promedio", precision = 10, scale = 2)
    private BigDecimal precioPromedio;

    // Referencia textual de ubicacion dentro del destino.
    @Column(length = 300)
    private String ubicacion;

    // Franja horaria de atencion o disponibilidad.
    @Column(length = 200)
    private String horarios;

    // Puntuacion global asignada por usuarios o sistema.
    @Column
    private Integer calificacion;

    // Relacion muchos-a-uno: varias opciones de alimentacion pertenecen a un
    // destino.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino", nullable = false)
    private Destino destino;

    // Bandera de borrado logico/visibilidad para mantener historial sin eliminar
    // fisicamente.
    @Column(nullable = false)
    @Builder.Default
    private Boolean activo = true;

    // Marca temporal de creacion del registro, gestionada automaticamente por
    // Hibernate.
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Marca temporal de ultima actualizacion, recalculada automaticamente en cada
    // cambio.
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
