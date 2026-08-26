package com.echeq.dto.request.usuario;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CambiarRolUsuarioRequest {

    @NotNull(message = "El rol es obligatorio")
    @Positive(message = "El rol debe ser válido")
    private Long rolId;

    public CambiarRolUsuarioRequest() {
    }

    public Long getRolId() {
        return rolId;
    }

    public void setRolId(Long rolId) {
        this.rolId = rolId;
    }
}