package com.echeq.entity;

import com.echeq.common.BaseEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "cuenta")
public class Cuenta extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String numeroCuenta;

    @Column(nullable = false)
    private Double saldo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banco_id", nullable = false)
    private Banco banco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "cuenta")
    private List<CuentaBanco> cuentasBanco;

    public Cuenta() {
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Banco getBanco() {
        return banco;
    }

    public void setBanco(Banco banco) {
        this.banco = banco;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<CuentaBanco> getCuentasBanco() {
        return cuentasBanco;
    }

    public void setCuentasBanco(List<CuentaBanco> cuentasBanco) {
        this.cuentasBanco = cuentasBanco;
    }
}