package com.echeq.controller;

import com.echeq.dto.request.cuentaBanco.ActualizarCuentaBancoRequest;
import com.echeq.dto.request.cuentaBanco.CrearCuentaBancoRequest;
import com.echeq.dto.response.cuentaBanco.CuentaBancoResponse;
import com.echeq.service.CuentaBancoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas-banco")
public class CuentaBancoController {

    private final CuentaBancoService cuentaBancoService;

    public CuentaBancoController(
            CuentaBancoService cuentaBancoService) {

        this.cuentaBancoService = cuentaBancoService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')")
    public ResponseEntity<List<CuentaBancoResponse>>
    obtenerTodas() {

        return ResponseEntity.ok(
                cuentaBancoService.obtenerTodas()
        );
    }

    @GetMapping("/mis-cuentas-banco")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CuentaBancoResponse>>
    obtenerMisCuentasBanco() {

        return ResponseEntity.ok(
                cuentaBancoService.obtenerMisCuentasBanco()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR', 'CLIENTE')"
    )
    public ResponseEntity<CuentaBancoResponse> obtenerPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                cuentaBancoService.obtenerPorId(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaBancoResponse> crear(
            @Valid @RequestBody CrearCuentaBancoRequest request) {

        CuentaBancoResponse response =
                cuentaBancoService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/mis-cuentas-banco")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CuentaBancoResponse> crearMiCuentaBanco(
            @Valid @RequestBody CrearCuentaBancoRequest request) {

        CuentaBancoResponse response =
                cuentaBancoService.crearMiCuentaBanco(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaBancoResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCuentaBancoRequest request) {

        return ResponseEntity.ok(
                cuentaBancoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        cuentaBancoService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}