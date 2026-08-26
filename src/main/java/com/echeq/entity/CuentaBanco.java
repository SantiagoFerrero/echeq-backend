package com.echeq.entity;

import com.echeq.common.BaseEntity;
import com.echeq.enums.EstadoCuentaBanco;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "cuenta_banco")
public class CuentaBanco extends BaseEntity {

    private LocalDate fechaAlta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoCuentaBanco estado;

    @ManyToOne
    @JoinColumn(name = "cuenta_id", nullable = false)
    private Cuenta cuenta;

    @ManyToOne
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;

    public CuentaBanco() {}

    public LocalDate getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(LocalDate fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public EstadoCuentaBanco getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuentaBanco estado) {
        this.estado = estado;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }
}