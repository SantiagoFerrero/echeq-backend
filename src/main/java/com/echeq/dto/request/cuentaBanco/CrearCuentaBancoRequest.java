package com.echeq.dto.request.cuentaBanco;

import jakarta.validation.constraints.NotNull;

public class CrearCuentaBancoRequest {

    @NotNull
    private Long cuentaId;

    @NotNull
    private Long bancoId;

    public CrearCuentaBancoRequest() {}

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    public Long getBancoId() {
        return bancoId;
    }

    public void setBancoId(Long bancoId) {
        this.bancoId = bancoId;
    }
}