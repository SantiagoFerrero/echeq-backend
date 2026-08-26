package com.echeq.entity;

import com.echeq.common.BaseEntity;
import com.echeq.enums.EstadoSolicitud;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado")
public class HistorialEstado extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "solicitud_id", nullable = false)
    private SolicitudECheq solicitudECheq;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoSolicitud estadoNuevo;

    private String observacion;

    private LocalDateTime fechaCambio;

    public HistorialEstado() {}

    public SolicitudECheq getSolicitudECheq() { return solicitudECheq; }
    public void setSolicitudECheq(SolicitudECheq solicitudECheq) { this.solicitudECheq = solicitudECheq; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public EstadoSolicitud getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(EstadoSolicitud estadoAnterior) { this.estadoAnterior = estadoAnterior; }

    public EstadoSolicitud getEstadoNuevo() { return estadoNuevo; }
    public void setEstadoNuevo(EstadoSolicitud estadoNuevo) { this.estadoNuevo = estadoNuevo; }

    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }

    public LocalDateTime getFechaCambio() {
        return fechaCambio;
    }

    public void setFechaCambio(LocalDateTime fechaCambio) {
        this.fechaCambio = fechaCambio;
    }
}
