package com.echeq.dto.request.solicitudEcheq;

import com.echeq.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CambiarEstadoSolicitudECheqRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoSolicitud estado;

    @Size(
            max = 500,
            message = "La observación no puede superar los 500 caracteres"
    )
    private String observacion;

    public CambiarEstadoSolicitudECheqRequest() {
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}