package com.echeq.service;

import com.echeq.dto.response.notificacion.NotificacionResponse;
import com.echeq.entity.Notificacion;
import com.echeq.entity.SolicitudECheq;
import com.echeq.entity.Usuario;
import com.echeq.enums.EstadoSolicitud;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.NotificacionMapper;
import com.echeq.repository.NotificacionRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificacionService {

    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionMapper mapper;

    public NotificacionService(
            NotificacionRepository notificacionRepository,
            UsuarioRepository usuarioRepository,
            NotificacionMapper mapper) {

        this.notificacionRepository =
                notificacionRepository;

        this.usuarioRepository =
                usuarioRepository;

        this.mapper =
                mapper;
    }

    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new SecurityException(
                    "No existe un usuario autenticado"
            );
        }

        return usuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado no encontrado"
                        )
                );
    }

    public List<NotificacionResponse>
    obtenerMisNotificaciones() {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        return notificacionRepository
                .findByUsuario_IdOrderByFechaEnvioDesc(
                        usuario.getId()
                )
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public NotificacionResponse marcarComoLeida(
            Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        Notificacion notificacion =
                notificacionRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notificación no encontrada con id: "
                                                + id
                                )
                        );

        if (!notificacion
                .getUsuario()
                .getId()
                .equals(usuario.getId())) {

            throw new SecurityException(
                    "No tiene permisos para modificar esta notificación"
            );
        }

        notificacion.setLeida(
                true
        );

        notificacion =
                notificacionRepository.save(
                        notificacion
                );

        return mapper.toResponse(
                notificacion
        );
    }

    @Transactional
    public void crearPorCambioEstado(
            SolicitudECheq solicitud,
            EstadoSolicitud nuevoEstado) {

        String estadoTexto =
                nuevoEstado == EstadoSolicitud.APROBADA
                        ? "aprobada"
                        : "rechazada";

        String mensaje =
                "Su solicitud eCheq #"
                        + solicitud.getId()
                        + " fue "
                        + estadoTexto
                        + ".";

        Notificacion notificacion =
                mapper.toEntity(
                        solicitud,
                        mensaje
                );

        notificacionRepository.save(
                notificacion
        );
    }
}