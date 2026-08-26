package com.echeq.dto.request.rol;

import com.echeq.enums.NombreRol;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ActualizarRolRequest {

    @NotNull
    private NombreRol nombre;

    @Size(max = 255)
    private String descripcion;

    public ActualizarRolRequest() {
    }

    public NombreRol getNombre() {
        return nombre;
    }

    public void setNombre(NombreRol nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}