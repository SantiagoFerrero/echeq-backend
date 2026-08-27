package com.echeq.controller;

import com.echeq.dto.request.solicitudEcheq.ActualizarSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CambiarEstadoSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CrearSolicitudECheqRequest;
import com.echeq.dto.response.solicitudEcheq.SolicitudECheqResponse;
import com.echeq.service.SolicitudECheqService;
import com.echeq.enums.EstadoSolicitud;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping("/api/solicitudes")
public class SolicitudECheqController {

    private final SolicitudECheqService service;

    public SolicitudECheqController(
            SolicitudECheqService service) {

        this.service = service;
    }

    @GetMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR')"
    )
    public ResponseEntity<List<SolicitudECheqResponse>>
    obtenerTodas(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta,
            @RequestParam(required = false)
            EstadoSolicitud estado,
            @RequestParam(required = false)
            String concepto) {

        return ResponseEntity.ok(
                service.filtrarSolicitudes(
                        usuarioId,
                        fechaDesde,
                        fechaHasta,
                        estado,
                        concepto
                )
        );
    }

    @GetMapping("/mis-solicitudes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<SolicitudECheqResponse>>
    obtenerMisSolicitudes(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta,
            @RequestParam(required = false)
            EstadoSolicitud estado,
            @RequestParam(required = false)
            String concepto) {

        return ResponseEntity.ok(
                service.filtrarMisSolicitudes(
                        fechaDesde,
                        fechaHasta,
                        estado,
                        concepto
                )
        );
    }

    @GetMapping("/exportar")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR')"
    )
    public ResponseEntity<byte[]> exportar(
            @RequestParam(required = false) Long usuarioId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaDesde,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaHasta,
            @RequestParam(required = false)
            EstadoSolicitud estado,
            @RequestParam(required = false)
            String concepto) {

        byte[] archivo = service.exportarSolicitudes(
                usuarioId,
                fechaDesde,
                fechaHasta,
                estado,
                concepto
        );

        String nombreArchivo =
                "solicitudes_echeq_" + LocalDate.now() + ".xlsx";

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + nombreArchivo
                )
                .contentType(
                        MediaType.parseMediaType(
                                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        )
                )
                .contentLength(archivo.length)
                .body(archivo);
    }

    @GetMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'AUDITOR', 'CLIENTE')"
    )
    public ResponseEntity<SolicitudECheqResponse> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                service.obtenerPorId(id)
        );
    }

    @PostMapping
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'CLIENTE')"
    )
    public ResponseEntity<SolicitudECheqResponse> crear(
            @Valid @RequestBody CrearSolicitudECheqRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.crear(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR', 'CLIENTE')"
    )
    public ResponseEntity<SolicitudECheqResponse> actualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ActualizarSolicitudECheqRequest request) {

        return ResponseEntity.ok(
                service.actualizar(id, request)
        );
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR')"
    )
    public ResponseEntity<SolicitudECheqResponse> actualizarEstado(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CambiarEstadoSolicitudECheqRequest request) {

        return ResponseEntity.ok(
                service.actualizarEstado(id, request)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize(
            "hasAnyRole('ADMIN', 'OPERADOR')"
    )
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        service.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}