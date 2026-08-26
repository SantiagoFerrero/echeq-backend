package com.echeq.mapper;

import com.echeq.dto.request.cuentaBanco.ActualizarCuentaBancoRequest;
import com.echeq.dto.response.cuentaBanco.CuentaBancoResponse;
import com.echeq.entity.Banco;
import com.echeq.entity.Cuenta;
import com.echeq.entity.CuentaBanco;
import com.echeq.enums.EstadoCuentaBanco;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CuentaBancoMapper {

    public CuentaBanco toEntity(
            Cuenta cuenta,
            Banco banco) {

        CuentaBanco cuentaBanco = new CuentaBanco();

        cuentaBanco.setFechaAlta(LocalDate.now());
        cuentaBanco.setEstado(EstadoCuentaBanco.ACTIVA);
        cuentaBanco.setCuenta(cuenta);
        cuentaBanco.setBanco(banco);

        return cuentaBanco;
    }

    public void updateEntity(
            CuentaBanco cuentaBanco,
            ActualizarCuentaBancoRequest request) {

        cuentaBanco.setEstado(request.getEstado());
    }

    public CuentaBancoResponse toResponse(
            CuentaBanco cuentaBanco) {

        CuentaBancoResponse response =
                new CuentaBancoResponse();

        response.setId(cuentaBanco.getId());

        response.setCuentaId(
                cuentaBanco.getCuenta().getId()
        );

        response.setNumeroCuenta(
                cuentaBanco.getCuenta().getNumeroCuenta()
        );

        if (cuentaBanco.getCuenta().getUsuario() != null) {

            response.setUsuarioId(
                    cuentaBanco.getCuenta()
                            .getUsuario()
                            .getId()
            );

            String nombreCompleto =
                    cuentaBanco.getCuenta()
                            .getUsuario()
                            .getNombre();

            if (cuentaBanco.getCuenta()
                    .getUsuario()
                    .getApellido() != null
                    && !cuentaBanco.getCuenta()
                    .getUsuario()
                    .getApellido()
                    .isBlank()) {

                nombreCompleto += " "
                        + cuentaBanco.getCuenta()
                        .getUsuario()
                        .getApellido();
            }

            response.setUsuarioNombre(nombreCompleto);
        }

        response.setBancoId(
                cuentaBanco.getBanco().getId()
        );

        response.setNombreBanco(
                cuentaBanco.getBanco().getNombre()
        );

        response.setEstado(
                cuentaBanco.getEstado()
        );

        response.setFechaAlta(
                cuentaBanco.getFechaAlta()
        );

        return response;
    }
}