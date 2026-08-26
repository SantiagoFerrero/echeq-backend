package com.echeq.mapper;

import com.echeq.dto.request.historialEstado.CrearHistorialEstadoRequest;
import com.echeq.dto.response.historialEstado.HistorialEstadoResponse;
import com.echeq.entity.HistorialEstado;
import com.echeq.entity.SolicitudECheq;
import com.echeq.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class historialEstadoMapper {

    public HistorialEstado toEntity(CrearHistorialEstadoRequest request,
                                    SolicitudECheq solicitud,
                                    Usuario usuario) {

        HistorialEstado h = new HistorialEstado();

        h.setSolicitudECheq(solicitud);
        h.setUsuario(usuario);
        h.setEstadoAnterior(request.getEstadoAnterior());
        h.setEstadoNuevo(request.getEstadoNuevo());
        h.setObservacion(request.getObservacion());
        h.setFechaCambio(LocalDateTime.now());

        return h;
    }

    public HistorialEstadoResponse toResponse(HistorialEstado h) {

        HistorialEstadoResponse r = new HistorialEstadoResponse();

        r.setId(h.getId());
        r.setSolicitudId(h.getSolicitudECheq().getId());
        r.setUsuarioId(h.getUsuario().getId());
        r.setEstadoAnterior(h.getEstadoAnterior());
        r.setEstadoNuevo(h.getEstadoNuevo());
        r.setObservacion(h.getObservacion());
        r.setFechaCambio(h.getFechaCambio());

        return r;
    }
}