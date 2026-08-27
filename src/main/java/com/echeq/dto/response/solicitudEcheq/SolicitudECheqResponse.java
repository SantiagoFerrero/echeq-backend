package com.echeq.dto.response.solicitudEcheq;

import com.echeq.enums.EstadoSolicitud;

import java.time.LocalDateTime;

public class SolicitudECheqResponse {

    private Long id;
    private Double monto;
    private String concepto;
    private LocalDateTime fechaSolicitud;

    private Long usuarioId;
    private String usuarioNombre;

    private Long cuentaCorrienteId;
    private String cuentaCorrienteAlias;
    private String cuentaCorrienteNumero;
    private String cbu;

    private Long bancoId;
    private String bancoNombre;

    private EstadoSolicitud estado;

    public SolicitudECheqResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getConcepto() {
        return concepto;
    }

    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
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

    public Long getCuentaCorrienteId() {
        return cuentaCorrienteId;
    }

    public void setCuentaCorrienteId(Long cuentaCorrienteId) {
        this.cuentaCorrienteId = cuentaCorrienteId;
    }

    public String getCuentaCorrienteAlias() {
        return cuentaCorrienteAlias;
    }

    public void setCuentaCorrienteAlias(String cuentaCorrienteAlias) {
        this.cuentaCorrienteAlias = cuentaCorrienteAlias;
    }

    public String getCuentaCorrienteNumero() {
        return cuentaCorrienteNumero;
    }

    public void setCuentaCorrienteNumero(String cuentaCorrienteNumero) {
        this.cuentaCorrienteNumero = cuentaCorrienteNumero;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public String getBancoNombre() {
        return bancoNombre;
    }

    public void setBancoNombre(String bancoNombre) {
        this.bancoNombre = bancoNombre;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }
}