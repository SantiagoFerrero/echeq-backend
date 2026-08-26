package com.echeq.dto.request.cuentaCorriente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class CrearCuentaCorrienteRequest {

    @NotNull(message = "La cuenta bancaria es obligatoria")
    @Positive(message = "El id de la cuenta bancaria debe ser mayor que cero")
    private Long cuentaBancoId;

    @NotBlank(message = "El CBU es obligatorio")
    @Pattern(
            regexp = "\\d{22}",
            message = "El CBU debe contener exactamente 22 dígitos"
    )
    private String cbu;

    @NotBlank(message = "El alias es obligatorio")
    @Size(
            max = 50,
            message = "El alias no puede superar los 50 caracteres"
    )
    private String alias;

    @NotBlank(message = "El número de cuenta corriente es obligatorio")
    @Size(
            max = 255,
            message = "El número de cuenta corriente no puede superar los 255 caracteres"
    )
    private String numeroCuentaCorriente;

    @NotNull(message = "El límite de descubierto es obligatorio")
    @PositiveOrZero(
            message = "El límite de descubierto no puede ser negativo"
    )
    private Double limiteDescubierto;

    public CrearCuentaCorrienteRequest() {
    }

    public Long getCuentaBancoId() {
        return cuentaBancoId;
    }

    public void setCuentaBancoId(Long cuentaBancoId) {
        this.cuentaBancoId = cuentaBancoId;
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

    public String getNumeroCuentaCorriente() {
        return numeroCuentaCorriente;
    }

    public void setNumeroCuentaCorriente(String numeroCuentaCorriente) {
        this.numeroCuentaCorriente = numeroCuentaCorriente;
    }

    public Double getLimiteDescubierto() {
        return limiteDescubierto;
    }

    public void setLimiteDescubierto(Double limiteDescubierto) {
        this.limiteDescubierto = limiteDescubierto;
    }
}