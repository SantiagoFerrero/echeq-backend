package com.echeq.mapper;

import com.echeq.dto.response.aprobacion.AprobacionResponse;
import com.echeq.entity.Aprobacion;
import org.springframework.stereotype.Component;

@Component
public class AprobacionMapper {

    public AprobacionResponse toResponse(Aprobacion aprobacion) {

        AprobacionResponse response =
                new AprobacionResponse();

        response.setId(
                aprobacion.getId()
        );

        response.setSolicitudId(
                aprobacion.getSolicitud().getId()
        );

        response.setUsuarioId(
                aprobacion.getUsuario().getId()
        );

        String nombreCompleto =
                aprobacion.getUsuario().getNombre();

        if (aprobacion.getUsuario().getApellido() != null
                && !aprobacion.getUsuario()
                .getApellido()
                .isBlank()) {

            nombreCompleto += " "
                    + aprobacion.getUsuario()
                    .getApellido();
        }

        response.setUsuarioNombre(
                nombreCompleto
        );

        response.setDecision(
                aprobacion.getDecision()
        );

        response.setFechaDecision(
                aprobacion.getFechaDecision()
        );

        response.setObservacion(
                aprobacion.getObservacion()
        );

        return response;
    }
}