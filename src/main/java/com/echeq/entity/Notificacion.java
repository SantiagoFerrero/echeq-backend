package com.echeq.entity;

import com.echeq.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacion")
public class Notificacion extends BaseEntity {

    @Column(
            name = "fecha_envio",
            nullable = false
    )
    private LocalDateTime fechaEnvio;

    @Column(
            nullable = false,
            length = 500
    )
    private String mensaje;

    @Column(nullable = false)
    private Boolean leida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "usuario_id",
            nullable = false
    )
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "solicitud_echeq_id",
            nullable = false
    )
    private SolicitudECheq solicitud;

    public Notificacion() {
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDateTime fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Boolean getLeida() {
        return leida;
    }

    public void setLeida(Boolean leida) {
        this.leida = leida;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public SolicitudECheq getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudECheq solicitud) {
        this.solicitud = solicitud;
    }
}