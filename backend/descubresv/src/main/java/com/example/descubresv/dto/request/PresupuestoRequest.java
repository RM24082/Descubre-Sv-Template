package com.example.descubresv.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

// Datos para crear o actualizar el presupuesto de un itinerario
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresupuestoRequest {

    private BigDecimal costoTransporte;
    private BigDecimal costoAlimentacion;
    private BigDecimal costoEntradas;
    private BigDecimal costoHospedaje;
    private BigDecimal costoOtros;
}
