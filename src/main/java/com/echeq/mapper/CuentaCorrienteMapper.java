package com.echeq.mapper;

import com.echeq.dto.request.cuentaCorriente.ActualizarCuentaCorrienteRequest;
import com.echeq.dto.request.cuentaCorriente.CrearCuentaCorrienteRequest;
import com.echeq.dto.response.cuentaCorriente.CuentaCorrienteResponse;
import com.echeq.entity.Cuenta;
import com.echeq.entity.CuentaBanco;
import com.echeq.entity.CuentaCorriente;
import com.echeq.entity.Usuario;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CuentaCorrienteMapper {

    public CuentaCorriente toEntity(
            CrearCuentaCorrienteRequest request,
            CuentaBanco cuentaBanco) {

        CuentaCorriente cuentaCorriente =
                new CuentaCorriente();

        cuentaCorriente.setCbu(
                request.getCbu().trim()
        );

        cuentaCorriente.setAlias(
                request.getAlias().trim()
        );

        cuentaCorriente.setFechaApertura(
                LocalDate.now()
        );

        cuentaCorriente.setLimiteDescubierto(
                request.getLimiteDescubierto()
        );

        cuentaCorriente.setNumeroCuentaCorriente(
                request.getNumeroCuentaCorriente().trim()
        );

        cuentaCorriente.setCuentaBanco(
                cuentaBanco
        );

        return cuentaCorriente;
    }

    public void updateEntity(
            CuentaCorriente cuentaCorriente,
            ActualizarCuentaCorrienteRequest request) {

        cuentaCorriente.setCbu(
                request.getCbu().trim()
        );

        cuentaCorriente.setAlias(
                request.getAlias().trim()
        );

        cuentaCorriente.setLimiteDescubierto(
                request.getLimiteDescubierto()
        );

        cuentaCorriente.setNumeroCuentaCorriente(
                request.getNumeroCuentaCorriente().trim()
        );
    }

    public CuentaCorrienteResponse toResponse(
            CuentaCorriente cuentaCorriente) {

        CuentaCorrienteResponse response =
                new CuentaCorrienteResponse();

        response.setId(
                cuentaCorriente.getId()
        );

        CuentaBanco cuentaBanco =
                cuentaCorriente.getCuentaBanco();

        if (cuentaBanco != null) {

            response.setCuentaBancoId(
                    cuentaBanco.getId()
            );

            if (cuentaBanco.getBanco() != null) {

                response.setBancoId(
                        cuentaBanco.getBanco().getId()
                );

                response.setNombreBanco(
                        cuentaBanco.getBanco().getNombre()
                );
            }

            Cuenta cuenta =
                    cuentaBanco.getCuenta();

            if (cuenta != null) {

                response.setCuentaId(
                        cuenta.getId()
                );

                response.setNumeroCuenta(
                        cuenta.getNumeroCuenta()
                );

                Usuario usuario =
                        cuenta.getUsuario();

                if (usuario != null) {

                    response.setUsuarioId(
                            usuario.getId()
                    );

                    String nombreCompleto =
                            usuario.getNombre();

                    if (usuario.getApellido() != null
                            && !usuario.getApellido().isBlank()) {

                        nombreCompleto += " "
                                + usuario.getApellido();
                    }

                    response.setUsuarioNombre(
                            nombreCompleto
                    );
                }
            }
        }

        response.setCbu(
                cuentaCorriente.getCbu()
        );

        response.setAlias(
                cuentaCorriente.getAlias()
        );

        response.setFechaApertura(
                cuentaCorriente.getFechaApertura()
        );

        response.setLimiteDescubierto(
                cuentaCorriente.getLimiteDescubierto()
        );

        response.setNumeroCuentaCorriente(
                cuentaCorriente.getNumeroCuentaCorriente()
        );

        return response;
    }
}