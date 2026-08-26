package com.echeq.service;

import com.echeq.dto.request.historialEstado.CrearHistorialEstadoRequest;
import com.echeq.dto.response.historialEstado.HistorialEstadoResponse;
import com.echeq.entity.HistorialEstado;
import com.echeq.entity.SolicitudECheq;
import com.echeq.entity.Usuario;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.historialEstadoMapper;
import com.echeq.repository.HistorialEstadoRepository;
import com.echeq.repository.SolicitudECheqRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistorialEstadoService {

    private final HistorialEstadoRepository historialRepository;
    private final SolicitudECheqRepository solicitudRepository;
    private final UsuarioRepository usuarioRepository;
    private final historialEstadoMapper mapper;

    public HistorialEstadoService(HistorialEstadoRepository historialRepository,
                                  SolicitudECheqRepository solicitudRepository,
                                  UsuarioRepository usuarioRepository,
                                  historialEstadoMapper mapper) {
        this.historialRepository = historialRepository;
        this.solicitudRepository = solicitudRepository;
        this.usuarioRepository = usuarioRepository;
        this.mapper = mapper;
    }

    public List<HistorialEstadoResponse> obtenerTodos() {
        return historialRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public HistorialEstadoResponse obtenerPorId(Long id) {

        HistorialEstado h = historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado"));

        return mapper.toResponse(h);
    }

    @Transactional
    public HistorialEstadoResponse crear(CrearHistorialEstadoRequest request) {

        SolicitudECheq solicitud = solicitudRepository.findById(request.getSolicitudId())
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        // VALIDACIÓN REAL DE ESTADOS
        if (request.getEstadoAnterior() == request.getEstadoNuevo()) {
            throw new IllegalArgumentException("El estado no puede ser el mismo");
        }

        HistorialEstado historial = mapper.toEntity(request, solicitud, usuario);

        historial = historialRepository.save(historial);

        return mapper.toResponse(historial);
    }

    @Transactional
    public void eliminar(Long id) {

        HistorialEstado h = historialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historial no encontrado"));

        historialRepository.delete(h);
    }
}