package com.echeq.dto.response.rol;

import com.echeq.enums.NombreRol;

public class RolResponse {

    private Long id;

    private NombreRol nombre;

    private String descripcion;

    public RolResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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