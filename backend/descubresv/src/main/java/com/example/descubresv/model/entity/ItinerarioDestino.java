package com.example.descubresv.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

// Entidad itinerario destino - relacion muchos a muchos con orden y dia
@Entity
@Table(name = "itinerario_destinos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(ItinerarioDestinoId.class)
public class ItinerarioDestino {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_itinerario")
    private Itinerario itinerario;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_destino")
    private Destino destino;

    @Column(name = "dia_numero", nullable = false)
    @Builder.Default
    private Integer diaNumero = 1;

    @Column(nullable = false)
    @Builder.Default
    private Integer orden = 1;

    @Column(columnDefinition = "TEXT")
    private String notas;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
