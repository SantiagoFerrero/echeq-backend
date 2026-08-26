package com.echeq.controller;

import com.echeq.dto.response.aprobacion.AprobacionResponse;
import com.echeq.service.AprobacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/aprobaciones")
public class AprobacionController {

    private final AprobacionService aprobacionService;

    public AprobacionController(
            AprobacionService aprobacionService) {

        this.aprobacionService =
                aprobacionService;
    }

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')"
    )
    public ResponseEntity<List<AprobacionResponse>>
    obtenerTodas() {

        return ResponseEntity.ok(
                aprobacionService.obtenerTodas()
        );
    }

    @GetMapping("/solicitud/{solicitudId}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')"
    )
    public ResponseEntity<AprobacionResponse>
    obtenerPorSolicitud(
            @PathVariable @Positive Long solicitudId) {

        return ResponseEntity.ok(
                aprobacionService
                        .obtenerPorSolicitud(solicitudId)
        );
    }
}