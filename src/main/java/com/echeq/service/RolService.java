package com.echeq.service;

import com.echeq.dto.request.rol.ActualizarRolRequest;
import com.echeq.dto.request.rol.CrearRolRequest;
import com.echeq.dto.response.rol.RolResponse;
import com.echeq.entity.Rol;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.RolMapper;
import com.echeq.repository.RolRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolService {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public RolService(RolRepository rolRepository,
                      RolMapper rolMapper) {
        this.rolRepository = rolRepository;
        this.rolMapper = rolMapper;
    }

    public List<RolResponse> obtenerTodos() {
        return rolRepository.findAll()
                .stream()
                .map(rolMapper::toResponse)
                .toList();
    }

    public RolResponse obtenerPorId(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        return rolMapper.toResponse(rol);
    }

    @Transactional
    public RolResponse crear(CrearRolRequest request) {

        Rol rol = rolMapper.toEntity(request);

        rol = rolRepository.save(rol);

        return rolMapper.toResponse(rol);
    }

    @Transactional
    public RolResponse actualizar(Long id, ActualizarRolRequest request) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        rolMapper.updateEntity(rol, request);

        rol = rolRepository.save(rol);

        return rolMapper.toResponse(rol);
    }

    @Transactional
    public void eliminar(Long id) {

        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado"));

        rolRepository.delete(rol);
    }
}