package com.echeq.controller;

import com.echeq.dto.request.auth.LoginRequest;
import com.echeq.dto.request.auth.RegistroRequest;
import com.echeq.dto.response.auth.LoginResponse;
import com.echeq.dto.response.usuario.UsuarioResponse;
import com.echeq.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponse> registrar(
            @Valid @RequestBody RegistroRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.registrar(request));
    }
}