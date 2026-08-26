package com.echeq.dto.request.cuentaBanco;

import com.echeq.enums.EstadoCuentaBanco;
import jakarta.validation.constraints.NotNull;

public class ActualizarCuentaBancoRequest {

    @NotNull
    private EstadoCuentaBanco estado;

    public ActualizarCuentaBancoRequest() {}

    public EstadoCuentaBanco getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuentaBanco estado) {
        this.estado = estado;
    }
}