package com.echeq.entity;

import com.echeq.common.BaseEntity;
import com.echeq.enums.DecisionAprobacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "aprobacion",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_aprobacion_solicitud",
                        columnNames = "solicitud_echeq_id"
                )
        }
)
public class Aprobacion extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "solicitud_echeq_id",
            nullable = false
    )
    private SolicitudECheq solicitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 20
    )
    private DecisionAprobacion decision;

    @Column(
            name = "fecha_decision",
            nullable = false
    )
    private LocalDateTime fechaDecision;

    @Column(length = 500)
    private String observacion;

    public Aprobacion() {
    }

    public SolicitudECheq getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudECheq solicitud) {
        this.solicitud = solicitud;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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