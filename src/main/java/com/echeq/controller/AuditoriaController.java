package com.echeq.controller;

import com.echeq.dto.response.auditoria.AuditoriaResponse;
import com.echeq.service.AuditoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditorias")
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    public AuditoriaController(
            AuditoriaService auditoriaService) {

        this.auditoriaService =
                auditoriaService;
    }

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'AUDITOR')"
    )
    public ResponseEntity<List<AuditoriaResponse>>
    obtenerTodas() {

        return ResponseEntity.ok(
                auditoriaService.obtenerTodas()
        );
    }
}