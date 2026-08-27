package com.echeq.mapper;

import com.echeq.dto.request.solicitudEcheq.ActualizarSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CambiarEstadoSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CrearSolicitudECheqRequest;
import com.echeq.dto.response.solicitudEcheq.SolicitudECheqResponse;
import com.echeq.entity.CuentaCorriente;
import com.echeq.entity.SolicitudECheq;
import com.echeq.entity.Usuario;
import com.echeq.enums.EstadoSolicitud;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class SolicitudECheqMapper {

    public SolicitudECheq toEntity(
            CrearSolicitudECheqRequest request,
            Usuario usuario,
            CuentaCorriente cuentaCorriente) {

        SolicitudECheq solicitud =
                new SolicitudECheq();

        solicitud.setMonto(
                request.getMonto()
        );

        solicitud.setConcepto(
                request.getConcepto().trim()
        );

        solicitud.setFechaSolicitud(
                LocalDateTime.now()
        );

        solicitud.setUsuario(
                usuario
        );

        solicitud.setCuentaCorriente(
                cuentaCorriente
        );

        solicitud.setEstado(
                EstadoSolicitud.PENDIENTE
        );

        return solicitud;
    }

    public void updateEntity(
            SolicitudECheq solicitud,
            ActualizarSolicitudECheqRequest request) {

        solicitud.setMonto(
                request.getMonto()
        );

        solicitud.setConcepto(
                request.getConcepto().trim()
        );
    }

    public void updateEstado(
            SolicitudECheq solicitud,
            CambiarEstadoSolicitudECheqRequest request) {

        solicitud.setEstado(
                request.getEstado()
        );
    }

    public SolicitudECheqResponse toResponse(
            SolicitudECheq solicitud) {

        SolicitudECheqResponse response =
                new SolicitudECheqResponse();

        response.setId(
                solicitud.getId()
        );

        response.setMonto(
                solicitud.getMonto()
        );

        response.setConcepto(
                solicitud.getConcepto()
        );

        response.setFechaSolicitud(
                solicitud.getFechaSolicitud()
        );

        if (solicitud.getUsuario() != null) {

            response.setUsuarioId(
                    solicitud.getUsuario().getId()
            );

            String nombreCompleto =
                    solicitud.getUsuario().getNombre();

            if (solicitud.getUsuario().getApellido() != null
                    && !solicitud.getUsuario()
                    .getApellido()
                    .isBlank()) {

                nombreCompleto += " "
                        + solicitud.getUsuario()
                        .getApellido();
            }

            response.setUsuarioNombre(
                    nombreCompleto
            );
        }

        if (solicitud.getCuentaCorriente() != null) {

            response.setCuentaCorrienteId(
                    solicitud.getCuentaCorriente().getId()
            );

            response.setCuentaCorrienteAlias(
                    solicitud.getCuentaCorriente().getAlias()
            );

            response.setCuentaCorrienteNumero(
                    solicitud.getCuentaCorriente().getNumeroCuentaCorriente()
            );

            response.setCbu(
                    solicitud.getCuentaCorriente().getCbu()
            );

            if (solicitud.getCuentaCorriente().getCuentaBanco() != null
                    && solicitud.getCuentaCorriente().getCuentaBanco().getBanco() != null) {

                response.setBancoId(
                        solicitud.getCuentaCorriente().getCuentaBanco().getBanco().getId()
                );

                response.setBancoNombre(
                        solicitud.getCuentaCorriente().getCuentaBanco().getBanco().getNombre()
                );
            }
        }

        response.setEstado(
                solicitud.getEstado()
        );

        return response;
    }
}