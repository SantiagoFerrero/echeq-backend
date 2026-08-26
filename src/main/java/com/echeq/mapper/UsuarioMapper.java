package com.echeq.mapper;

import com.echeq.dto.request.usuario.CrearUsuarioRequest;
import com.echeq.dto.response.usuario.UsuarioResponse;
import com.echeq.entity.Rol;
import com.echeq.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public Usuario toEntity(CrearUsuarioRequest request, Rol rol) {

        Usuario u = new Usuario();

        u.setNombre(request.getNombre());
        u.setApellido(request.getApellido());
        u.setEmail(request.getEmail());
        u.setPassword(request.getPassword());
        u.setActivo(true);
        u.setRol(rol);

        return u;
    }

    public UsuarioResponse toResponse(Usuario u) {

        UsuarioResponse r = new UsuarioResponse();

        r.setId(u.getId());
        r.setNombre(u.getNombre());
        r.setApellido(u.getApellido());
        r.setEmail(u.getEmail());
        r.setActivo(u.getActivo());
        r.setRolId(u.getRol().getId());

        return r;
    }
}