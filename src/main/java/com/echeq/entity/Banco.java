package com.echeq.entity;

import com.echeq.common.BaseEntity;
import com.echeq.enums.NombreRol;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "banco")
public class Banco extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String codigoBanco;

    @OneToMany(mappedBy = "banco")
    private List<CuentaBanco> cuentasBanco;

    @OneToMany(mappedBy = "banco")
    private List<Cuenta> cuentas;

    public Banco() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoBanco() {
        return codigoBanco;
    }

    public void setCodigoBanco(String codigoBanco) {
        this.codigoBanco = codigoBanco;
    }

    public List<CuentaBanco> getCuentasBanco() {
        return cuentasBanco;
    }

    public void setCuentasBanco(List<CuentaBanco> cuentasBanco) {
        this.cuentasBanco = cuentasBanco;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }
}