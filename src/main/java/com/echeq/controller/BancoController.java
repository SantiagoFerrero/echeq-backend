package com.echeq.controller;

import com.echeq.dto.request.banco.ActualizarBancoRequest;
import com.echeq.dto.request.banco.CrearBancoRequest;
import com.echeq.dto.response.banco.BancoResponse;
import com.echeq.service.BancoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/bancos")
public class BancoController {

    private final BancoService bancoService;

    public BancoController(BancoService bancoService) {
        this.bancoService = bancoService;
    }

    @GetMapping
    public ResponseEntity<List<BancoResponse>> obtenerTodos() {
        return ResponseEntity.ok(
                bancoService.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponse> obtenerPorId(
            @PathVariable @Positive Long id) {

        return ResponseEntity.ok(
                bancoService.obtenerPorId(id)
        );
    }

    @PostMapping
    public ResponseEntity<BancoResponse> crear(
            @Valid @RequestBody CrearBancoRequest request) {

        return ResponseEntity.ok(
                bancoService.crear(request)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoResponse> actualizar(
            @PathVariable @Positive Long id,
            @Valid @RequestBody ActualizarBancoRequest request) {

        return ResponseEntity.ok(
                bancoService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable @Positive Long id) {

        bancoService.eliminar(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}