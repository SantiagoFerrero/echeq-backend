package com.echeq.dto.request.cuenta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ActualizarCuentaRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 30, message = "El número de cuenta no puede superar los 30 caracteres")
    private String numeroCuenta;

    @NotNull(message = "El saldo es obligatorio")
    @Positive(message = "El saldo debe ser mayor a cero")
    private Double saldo;

    @NotNull(message = "Debe seleccionar un banco")
    private Long bancoId;

    @NotNull(message = "Debe seleccionar un usuario")
    private Long usuarioId;

    public ActualizarCuentaRequest() {
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

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }
}