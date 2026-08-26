package com.echeq.controller;

import com.echeq.dto.request.cuenta.ActualizarCuentaRequest;
import com.echeq.dto.request.cuenta.CrearCuentaRequest;
import com.echeq.dto.request.cuenta.CrearMiCuentaRequest;
import com.echeq.dto.response.cuenta.CuentaResponse;
import com.echeq.service.CuentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')")
    public ResponseEntity<List<CuentaResponse>> obtenerTodas() {

        return ResponseEntity.ok(
                cuentaService.obtenerTodas()
        );
    }

    @GetMapping("/mis-cuentas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CuentaResponse>> obtenerMisCuentas() {

        return ResponseEntity.ok(
                cuentaService.obtenerMisCuentas()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR', 'CLIENTE')"
    )
    public ResponseEntity<CuentaResponse> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                cuentaService.obtenerPorId(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaResponse> crear(
            @Valid @RequestBody CrearCuentaRequest request) {

        CuentaResponse response =
                cuentaService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/mis-cuentas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CuentaResponse> crearMiCuenta(
            @Valid @RequestBody CrearMiCuentaRequest request) {

        CuentaResponse response =
                cuentaService.crearMiCuenta(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaResponse> actualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ActualizarCuentaRequest request) {

        return ResponseEntity.ok(
                cuentaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        cuentaService.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}