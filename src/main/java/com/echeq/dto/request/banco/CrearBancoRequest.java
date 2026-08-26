package com.echeq.dto.request.banco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CrearBancoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    private String codigoBanco;

    public CrearBancoRequest() {
    }

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
}