package com.echeq.mapper;

import com.echeq.dto.response.auditoria.AuditoriaResponse;
import com.echeq.entity.Auditoria;
import org.springframework.stereotype.Component;

@Component
public class AuditoriaMapper {

    public AuditoriaResponse toResponse(
            Auditoria auditoria) {

        AuditoriaResponse response =
                new AuditoriaResponse();

        response.setId(
                auditoria.getId()
        );

        response.setAccion(
                auditoria.getAccion()
        );

        response.setDetalle(
                auditoria.getDetalle()
        );

        response.setFechaHora(
                auditoria.getCreatedAt()
        );

        if (auditoria.getUsuario() != null) {

            response.setUsuarioId(
                    auditoria.getUsuario().getId()
            );

            String nombreCompleto =
                    auditoria.getUsuario().getNombre();

            if (auditoria.getUsuario().getApellido() != null
                    && !auditoria.getUsuario()
                    .getApellido()
                    .isBlank()) {

                nombreCompleto += " "
                        + auditoria.getUsuario()
                        .getApellido();
            }

            response.setUsuarioNombre(
                    nombreCompleto
            );
        }

        return response;
    }
}