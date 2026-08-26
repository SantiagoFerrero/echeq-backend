package com.echeq.dto.response.aprobacion;

import com.echeq.enums.DecisionAprobacion;

import java.time.LocalDateTime;

public class AprobacionResponse {

    private Long id;

    private Long solicitudId;

    private Long usuarioId;

    private String usuarioNombre;

    private DecisionAprobacion decision;

    private LocalDateTime fechaDecision;

    private String observacion;

    public AprobacionResponse() {
    }

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

    public String getUsuarioNombre() {
        return usuarioNombre;
    }

    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public DecisionAprobacion getDecision() {
        return decision;
    }

    public void setDecision(DecisionAprobacion decision) {
        this.decision = decision;
    }

    public LocalDateTime getFechaDecision() {
        return fechaDecision;
    }

    public void setFechaDecision(LocalDateTime fechaDecision) {
        this.fechaDecision = fechaDecision;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }
}