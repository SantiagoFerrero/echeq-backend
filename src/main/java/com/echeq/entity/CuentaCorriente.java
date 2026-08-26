package com.echeq.entity;

import com.echeq.common.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cuenta_corriente")
public class CuentaCorriente extends BaseEntity {

    @Column(nullable = false, unique = true, length = 22)
    private String cbu;

    @Column(nullable = false, unique = true, length = 50)
    private String alias;

    @Column(name = "fecha_apertura", nullable = false)
    private LocalDate fechaApertura;

    @Column(name = "limite_descubierto", nullable = false)
    private Double limiteDescubierto;

    @Column(
            name = "numero_cuenta_corriente",
            nullable = false,
            length = 255
    )
    private String numeroCuentaCorriente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuenta_banco_id", nullable = false)
    private CuentaBanco cuentaBanco;

    public CuentaCorriente() {
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public LocalDate getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(LocalDate fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Double getLimiteDescubierto() {
        return limiteDescubierto;
    }

    public void setLimiteDescubierto(Double limiteDescubierto) {
        this.limiteDescubierto = limiteDescubierto;
    }

    public String getNumeroCuentaCorriente() {
        return numeroCuentaCorriente;
    }

    public void setNumeroCuentaCorriente(String numeroCuentaCorriente) {
        this.numeroCuentaCorriente = numeroCuentaCorriente;
    }

    public CuentaBanco getCuentaBanco() {
        return cuentaBanco;
    }

    public void setCuentaBanco(CuentaBanco cuentaBanco) {
        this.cuentaBanco = cuentaBanco;
    }
}