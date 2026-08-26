package com.echeq.mapper;

import com.echeq.dto.request.cuenta.ActualizarCuentaRequest;
import com.echeq.dto.request.cuenta.CrearCuentaRequest;
import com.echeq.dto.response.cuenta.CuentaResponse;
import com.echeq.entity.Cuenta;
import org.springframework.stereotype.Component;

@Component
public class CuentaMapper {

    public Cuenta toEntity(CrearCuentaRequest request) {

        Cuenta cuenta = new Cuenta();

        cuenta.setNumeroCuenta(
                request.getNumeroCuenta().trim()
        );

        cuenta.setSaldo(
                request.getSaldo()
        );

        return cuenta;
    }

    public CuentaResponse toResponse(Cuenta cuenta) {

        CuentaResponse response = new CuentaResponse();

        response.setId(cuenta.getId());
        response.setNumeroCuenta(cuenta.getNumeroCuenta());
        response.setSaldo(cuenta.getSaldo());

        if (cuenta.getBanco() != null) {

            response.setBancoId(
                    cuenta.getBanco().getId()
            );

            response.setBancoNombre(
                    cuenta.getBanco().getNombre()
            );
        }

        if (cuenta.getUsuario() != null) {

            response.setUsuarioId(
                    cuenta.getUsuario().getId()
            );

            String nombreCompleto =
                    cuenta.getUsuario().getNombre();

            if (cuenta.getUsuario().getApellido() != null
                    && !cuenta.getUsuario().getApellido().isBlank()) {

                nombreCompleto += " "
                        + cuenta.getUsuario().getApellido();
            }

            response.setUsuarioNombre(nombreCompleto);
        }

        return response;
    }

    public void updateEntity(
            Cuenta cuenta,
            ActualizarCuentaRequest request) {

        cuenta.setNumeroCuenta(
                request.getNumeroCuenta().trim()
        );

        cuenta.setSaldo(
                request.getSaldo()
        );
    }
}