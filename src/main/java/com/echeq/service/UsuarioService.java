package com.echeq.service;

import com.echeq.dto.request.usuario.CambiarRolUsuarioRequest;
import com.echeq.dto.request.usuario.CrearUsuarioRequest;
import com.echeq.dto.response.usuario.UsuarioResponse;
import com.echeq.entity.Rol;
import com.echeq.entity.Usuario;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.UsuarioMapper;
import com.echeq.repository.RolRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            RolRepository rolRepository,
            UsuarioMapper mapper,
            PasswordEncoder passwordEncoder) {

        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> obtenerTodos() {

        return usuarioRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public UsuarioResponse crear(
            CrearUsuarioRequest request) {

        Rol rol = rolRepository
                .findById(request.getRolId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Rol no encontrado"
                        )
                );

        Usuario usuario =
                mapper.toEntity(request, rol);

        usuario.setPassword(
                passwordEncoder.encode(
                        usuario.getPassword()
                )
        );

        usuario =
                usuarioRepository.save(usuario);

        return mapper.toResponse(usuario);
    }

    @Transactional
    public UsuarioResponse cambiarRol(
            Long usuarioId,
            CambiarRolUsuarioRequest request) {

        Usuario usuario =
                usuarioRepository
                        .findById(usuarioId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );

        Rol nuevoRol =
                rolRepository
                        .findById(request.getRolId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Rol no encontrado"
                                )
                        );

        usuario.setRol(nuevoRol);

        usuario =
                usuarioRepository.save(usuario);

        return mapper.toResponse(usuario);
    }

    @Transactional
    public void eliminar(Long id) {

        Usuario usuario =
                usuarioRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado"
                                )
                        );

        usuarioRepository.delete(usuario);
    }
}