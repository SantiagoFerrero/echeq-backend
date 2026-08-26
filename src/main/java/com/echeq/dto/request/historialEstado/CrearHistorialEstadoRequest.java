package com.echeq.dto.request.historialEstado;


import com.echeq.enums.EstadoSolicitud;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

public class CrearHistorialEstadoRequest {

    @NotNull
    private Long solicitudId;

    @NotNull
    private Long usuarioId;

    @NotNull
    private EstadoSolicitud estadoAnterior;

    @NotNull
    private EstadoSolicitud estadoNuevo;

    private String observacion;

    public CrearHistorialEstadoRequest() {}

    public Long getSolicitudId() {
        return solicitudId;
    }

    public void setSolicitudId(Long solicitudId) {
        this.solicitudId = solicitudId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public EstadoSolicitud getEstadoAnterior() {
        return estadoAnterior;
    }

    public void setEstadoAnterior(EstadoSolicitud estadoAnterior) {
        this.estadoAnterior = estadoAnterior;
    }

    public EstadoSolicitud getEstadoNuevo() {
        return estadoNuevo;
    }

    public void setEstadoNuevo(EstadoSolicitud estadoNuevo) {
        this.estadoNuevo = estadoNuevo;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}