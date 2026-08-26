package com.echeq.controller;

import com.echeq.dto.response.notificacion.NotificacionResponse;
import com.echeq.service.NotificacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(
            NotificacionService notificacionService) {

        this.notificacionService =
                notificacionService;
    }

    @GetMapping("/mis-notificaciones")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<NotificacionResponse>>
    obtenerMisNotificaciones() {

        return ResponseEntity.ok(
                notificacionService
                        .obtenerMisNotificaciones()
        );
    }

    @PatchMapping("/{id}/leida")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<NotificacionResponse>
    marcarComoLeida(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                notificacionService
                        .marcarComoLeida(id)
        );
    }
}