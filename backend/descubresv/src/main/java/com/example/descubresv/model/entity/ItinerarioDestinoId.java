package com.example.descubresv.model.entity;

import java.io.Serializable;
import java.util.Objects;

// Clase para la llave primaria compuesta de itinerario_destinos
public class ItinerarioDestinoId implements Serializable {

    private Long itinerario;
    private Long destino;

    public ItinerarioDestinoId() {
    }

    public ItinerarioDestinoId(Long itinerario, Long destino) {
        this.itinerario = itinerario;
        this.destino = destino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ItinerarioDestinoId that = (ItinerarioDestinoId) o;
        return Objects.equals(itinerario, that.itinerario) &&
                Objects.equals(destino, that.destino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itinerario, destino);
    }
}
