package com.echeq.dto.request.solicitudEcheq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ActualizarSolicitudECheqRequest {

    @NotNull(message = "La cuenta corriente es obligatoria")
    private Long cuentaCorrienteId;

    @NotNull(message = "El monto es obligatorio")
    @Positive(message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotBlank(message = "El concepto es obligatorio")
    @Size(
            max = 255,
            message = "El concepto no puede superar los 255 caracteres"
    )
    private String concepto;

    public ActualizarSolicitudECheqRequest() {
    }

    public Long getCuentaCorrienteId() {
        return cuentaCorrienteId;
    }

    public void setCuentaCorrienteId(Long cuentaCorrienteId) {
        this.cuentaCorrienteId = cuentaCorrienteId;
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
}