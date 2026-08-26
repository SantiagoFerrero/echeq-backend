package com.echeq.service;

import com.echeq.dto.response.aprobacion.AprobacionResponse;
import com.echeq.entity.Aprobacion;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.AprobacionMapper;
import com.echeq.repository.AprobacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AprobacionService {

    private final AprobacionRepository aprobacionRepository;
    private final AprobacionMapper mapper;

    public AprobacionService(
            AprobacionRepository aprobacionRepository,
            AprobacionMapper mapper) {

        this.aprobacionRepository =
                aprobacionRepository;

        this.mapper =
                mapper;
    }

    public List<AprobacionResponse> obtenerTodas() {

        return aprobacionRepository
                .findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public AprobacionResponse obtenerPorSolicitud(
            Long solicitudId) {

        Aprobacion aprobacion =
                aprobacionRepository
                        .findBySolicitud_Id(solicitudId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "No existe una decisión registrada para la solicitud: "
                                                + solicitudId
                                )
                        );

        return mapper.toResponse(
                aprobacion
        );
    }
}