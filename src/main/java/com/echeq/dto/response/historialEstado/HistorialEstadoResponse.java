package com.echeq.dto.response.historialEstado;


import com.echeq.enums.EstadoSolicitud;
import java.time.LocalDateTime;

public class HistorialEstadoResponse {

    private Long id;

    private Long solicitudId;
    private Long usuarioId;

    private EstadoSolicitud estadoAnterior;
    private EstadoSolicitud estadoNuevo;

    private String observacion;

    private LocalDateTime fechaCambio;

    public HistorialEstadoResponse() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}