package com.echeq.mapper;

import com.echeq.dto.response.notificacion.NotificacionResponse;
import com.echeq.entity.Notificacion;
import com.echeq.entity.SolicitudECheq;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificacionMapper {

    public Notificacion toEntity(
            SolicitudECheq solicitud,
            String mensaje) {

        Notificacion notificacion =
                new Notificacion();

        notificacion.setUsuario(
                solicitud.getUsuario()
        );

        notificacion.setSolicitud(
                solicitud
        );

        notificacion.setMensaje(
                mensaje
        );

        notificacion.setLeida(
                false
        );

        notificacion.setFechaEnvio(
                LocalDateTime.now()
        );

        return notificacion;
    }

    public NotificacionResponse toResponse(
            Notificacion notificacion) {

        NotificacionResponse response =
                new NotificacionResponse();

        response.setId(
                notificacion.getId()
        );

        response.setUsuarioId(
                notificacion.getUsuario().getId()
        );

        response.setSolicitudId(
                notificacion.getSolicitud().getId()
        );

        response.setMensaje(
                notificacion.getMensaje()
        );

        response.setLeida(
                notificacion.getLeida()
        );

        response.setFechaEnvio(
                notificacion.getFechaEnvio()
        );

        return response;
    }
}