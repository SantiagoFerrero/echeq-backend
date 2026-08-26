package com.echeq.controller;

import com.echeq.dto.request.rol.CrearRolRequest;
import com.echeq.dto.request.rol.ActualizarRolRequest;
import com.echeq.dto.response.rol.RolResponse;
import com.echeq.service.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public ResponseEntity<List<RolResponse>> obtenerTodos() {
        return ResponseEntity.ok(rolService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<RolResponse> crear(@RequestBody CrearRolRequest request) {
        return ResponseEntity.ok(rolService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RolResponse> actualizar(@PathVariable Long id,
                                                  @RequestBody ActualizarRolRequest request) {
        return ResponseEntity.ok(rolService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        rolService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
