package com.echeq.dto.response.auth;

public class LoginResponse {

    private String token;
    private Long usuarioId;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;

    public LoginResponse(
            String token,
            Long usuarioId,
            String nombre,
            String apellido,
            String email,
            String rol) {

        this.token = token;
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rol = rol;
    }

    public String getToken() {
        return token;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getRol() {
        return rol;
    }
}