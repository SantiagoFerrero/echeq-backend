package com.echeq.service;

import com.echeq.dto.response.auditoria.AuditoriaResponse;
import com.echeq.entity.Auditoria;
import com.echeq.entity.Usuario;
import com.echeq.enums.AccionAuditoria;
import com.echeq.mapper.AuditoriaMapper;
import com.echeq.repository.AuditoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final AuditoriaMapper mapper;

    public AuditoriaService(
            AuditoriaRepository auditoriaRepository,
            AuditoriaMapper mapper) {

        this.auditoriaRepository =
                auditoriaRepository;

        this.mapper =
                mapper;
    }

    public List<AuditoriaResponse> obtenerTodas() {

        return auditoriaRepository
                .findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public void registrar(
            AccionAuditoria accion,
            String detalle,
            Usuario usuario) {

        Auditoria auditoria =
                new Auditoria();

        auditoria.setAccion(
                accion
        );

        auditoria.setDetalle(
                detalle
        );

        auditoria.setFecha(
                LocalDate.now()
        );

        auditoria.setUsuario(
                usuario
        );

        auditoriaRepository.save(
                auditoria
        );
    }
}