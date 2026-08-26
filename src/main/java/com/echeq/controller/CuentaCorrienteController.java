package com.echeq.controller;

import com.echeq.dto.request.cuentaCorriente.ActualizarCuentaCorrienteRequest;
import com.echeq.dto.request.cuentaCorriente.CrearCuentaCorrienteRequest;
import com.echeq.dto.response.cuentaCorriente.CuentaCorrienteResponse;
import com.echeq.service.CuentaCorrienteService;
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
@RequestMapping("/api/cuentas-corrientes")
public class CuentaCorrienteController {

    private final CuentaCorrienteService cuentaCorrienteService;

    public CuentaCorrienteController(
            CuentaCorrienteService cuentaCorrienteService) {

        this.cuentaCorrienteService =
                cuentaCorrienteService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')")
    public ResponseEntity<List<CuentaCorrienteResponse>>
    obtenerTodas() {

        return ResponseEntity.ok(
                cuentaCorrienteService.obtenerTodas()
        );
    }

    @GetMapping("/mis-cuentas-corrientes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<CuentaCorrienteResponse>>
    obtenerMisCuentasCorrientes() {

        return ResponseEntity.ok(
                cuentaCorrienteService.obtenerMisCuentasCorrientes()
        );
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR', 'CLIENTE')"
    )
    public ResponseEntity<CuentaCorrienteResponse> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                cuentaCorrienteService.obtenerPorId(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaCorrienteResponse> crear(
            @Valid @RequestBody CrearCuentaCorrienteRequest request) {

        CuentaCorrienteResponse response =
                cuentaCorrienteService.crear(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/mis-cuentas-corrientes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<CuentaCorrienteResponse> crearMiCuentaCorriente(
            @Valid @RequestBody CrearCuentaCorrienteRequest request) {

        CuentaCorrienteResponse response =
                cuentaCorrienteService.crearMiCuentaCorriente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<CuentaCorrienteResponse> actualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ActualizarCuentaCorrienteRequest request) {

        return ResponseEntity.ok(
                cuentaCorrienteService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        cuentaCorrienteService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}