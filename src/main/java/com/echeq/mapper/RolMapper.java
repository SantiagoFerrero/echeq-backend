package com.echeq.mapper;

import com.echeq.dto.request.rol.ActualizarRolRequest;
import com.echeq.dto.request.rol.CrearRolRequest;
import com.echeq.dto.response.rol.RolResponse;
import com.echeq.entity.Rol;
import org.springframework.stereotype.Component;

@Component
public class RolMapper {

    public Rol toEntity(CrearRolRequest request){

        Rol rol = new Rol();

        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());

        return rol;
    }

    public void updateEntity(Rol rol,
                             ActualizarRolRequest request){

        rol.setNombre(request.getNombre());
        rol.setDescripcion(request.getDescripcion());

    }

    public RolResponse toResponse(Rol rol){

        RolResponse response = new RolResponse();

        response.setId(rol.getId());
        response.setNombre(rol.getNombre());
        response.setDescripcion(rol.getDescripcion());

        return response;

    }

}