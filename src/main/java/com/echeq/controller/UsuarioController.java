package com.echeq.controller;

import com.echeq.dto.request.usuario.CambiarRolUsuarioRequest;
import com.echeq.dto.request.usuario.CrearUsuarioRequest;
import com.echeq.dto.response.usuario.UsuarioResponse;
import com.echeq.service.UsuarioService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(
            UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> obtenerTodos() {

        return ResponseEntity.ok(
                usuarioService.obtenerTodos()
        );
    }

    @GetMapping("/clientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<List<UsuarioResponse>> obtenerClientesActivos() {

        return ResponseEntity.ok(
                usuarioService.obtenerClientesActivos()
        );
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(
            @Valid @RequestBody CrearUsuarioRequest request) {

        return ResponseEntity.ok(
                usuarioService.crear(request)
        );
    }

    @PatchMapping("/{id}/rol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> cambiarRol(
            @PathVariable @Positive Long id,
            @Valid @RequestBody CambiarRolUsuarioRequest request) {

        return ResponseEntity.ok(
                usuarioService.cambiarRol(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        usuarioService.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}