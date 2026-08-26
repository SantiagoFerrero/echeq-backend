package com.echeq.service;

import com.echeq.dto.request.auth.LoginRequest;
import com.echeq.dto.request.auth.RegistroRequest;
import com.echeq.dto.response.auth.LoginResponse;
import com.echeq.dto.response.usuario.UsuarioResponse;
import com.echeq.entity.Rol;
import com.echeq.entity.Usuario;
import com.echeq.enums.NombreRol;
import com.echeq.mapper.UsuarioMapper;
import com.echeq.repository.RolRepository;
import com.echeq.repository.UsuarioRepository;
import com.echeq.security.jwt.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.authenticationManager = authenticationManager;
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        request.getPassword()
                )
        );

        Usuario user = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Usuario no encontrado"
                        )
                );

        String token = jwtService.generateToken(
                user.getEmail(),
                user.getRol().getNombre()
        );

        return new LoginResponse(
                token,
                user.getId(),
                user.getNombre(),
                user.getApellido(),
                user.getEmail(),
                user.getRol().getNombre().name()
        );
    }

    @Transactional
    public UsuarioResponse registrar(RegistroRequest request) {

        String email = request.getEmail()
                .trim()
                .toLowerCase();

        if (usuarioRepository.existsByEmail(email)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un usuario registrado con ese email"
            );
        }

        Rol rolCliente = rolRepository
                .findByNombre(NombreRol.CLIENTE)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "El rol CLIENTE no se encuentra configurado"
                        )
                );

        Usuario usuario = new Usuario();

        usuario.setNombre(request.getNombre().trim());
        usuario.setApellido(request.getApellido().trim());
        usuario.setEmail(email);
        usuario.setPassword(
                passwordEncoder.encode(
                        request.getPassword()
                )
        );
        usuario.setActivo(true);
        usuario.setRol(rolCliente);

        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuario);
    }
}