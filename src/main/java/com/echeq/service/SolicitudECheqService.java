package com.echeq.service;

import com.echeq.dto.request.solicitudEcheq.ActualizarSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CambiarEstadoSolicitudECheqRequest;
import com.echeq.dto.request.solicitudEcheq.CrearSolicitudECheqRequest;
import com.echeq.dto.response.solicitudEcheq.SolicitudECheqResponse;
import com.echeq.entity.Aprobacion;
import com.echeq.entity.CuentaCorriente;
import com.echeq.entity.SolicitudECheq;
import com.echeq.entity.Usuario;
import com.echeq.enums.AccionAuditoria;
import com.echeq.enums.DecisionAprobacion;
import com.echeq.enums.EstadoSolicitud;
import com.echeq.enums.NombreRol;
import com.echeq.exception.BusinessException;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.SolicitudECheqMapper;
import com.echeq.repository.AprobacionRepository;
import com.echeq.repository.CuentaCorrienteRepository;
import com.echeq.repository.SolicitudECheqRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SolicitudECheqService {

    private final SolicitudECheqRepository solicitudRepository;
    private final AprobacionRepository aprobacionRepository;
    private final NotificacionService notificacionService;
    private final AuditoriaService auditoriaService;
    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final SolicitudECheqMapper solicitudECheqMapper;
    private final SolicitudECheqExcelService solicitudECheqExcelService;

    public SolicitudECheqService(
            SolicitudECheqRepository solicitudRepository,
            AprobacionRepository aprobacionRepository,
            NotificacionService notificacionService,
            AuditoriaService auditoriaService,
            CuentaCorrienteRepository cuentaCorrienteRepository,
            UsuarioRepository usuarioRepository,
            SolicitudECheqMapper solicitudECheqMapper,
            SolicitudECheqExcelService solicitudECheqExcelService) {

        this.solicitudRepository = solicitudRepository;
        this.aprobacionRepository = aprobacionRepository;
        this.notificacionService = notificacionService;
        this.auditoriaService = auditoriaService;
        this.cuentaCorrienteRepository = cuentaCorrienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.solicitudECheqMapper = solicitudECheqMapper;
        this.solicitudECheqExcelService = solicitudECheqExcelService;
    }

    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null) {

            throw new SecurityException(
                    "No existe un usuario autenticado"
            );
        }

        return usuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado no encontrado"
                        )
                );
    }

    private boolean esCliente(Usuario usuario) {

        return usuario.getRol() != null
                && usuario.getRol().getNombre()
                == NombreRol.CLIENTE;
    }

    private boolean esAdministrador(Usuario usuario) {

        return usuario.getRol() != null
                && usuario.getRol().getNombre()
                == NombreRol.ADMIN;
    }

    private boolean esOperador(Usuario usuario) {

        return usuario.getRol() != null
                && usuario.getRol().getNombre()
                == NombreRol.OPERADOR;
    }

    private boolean esAuditor(Usuario usuario) {

        return usuario.getRol() != null
                && usuario.getRol().getNombre()
                == NombreRol.AUDITOR;
    }

    public List<SolicitudECheqResponse> obtenerTodas() {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(usuario)
                && !esOperador(usuario)
                && !esAuditor(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para consultar todas las solicitudes"
            );
        }

        return solicitudRepository.findAll()
                .stream()
                .map(solicitudECheqMapper::toResponse)
                .toList();
    }

    public List<SolicitudECheqResponse> obtenerMisSolicitudes() {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!esCliente(usuario)) {

            throw new SecurityException(
                    "Esta operación está disponible únicamente para clientes"
            );
        }

        return solicitudRepository
                .findByUsuarioId(usuario.getId())
                .stream()
                .map(solicitudECheqMapper::toResponse)
                .toList();
    }


    public List<SolicitudECheqResponse> filtrarSolicitudes(
            Long usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            EstadoSolicitud estado,
            String concepto) {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (!esAdministrador(usuario)
                && !esOperador(usuario)
                && !esAuditor(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para consultar todas las solicitudes"
            );
        }

        validarRangoFechas(
                fechaDesde,
                fechaHasta
        );

        LocalDateTime desde =
                fechaDesde != null
                        ? fechaDesde.atStartOfDay()
                        : null;

        LocalDateTime hasta =
                fechaHasta != null
                        ? fechaHasta.plusDays(1).atStartOfDay()
                        : null;

        String conceptoNormalizado =
                concepto != null && !concepto.isBlank()
                        ? concepto.trim()
                        : null;

        return solicitudRepository
                .filtrar(
                        usuarioId,
                        desde,
                        hasta,
                        estado,
                        conceptoNormalizado
                )
                .stream()
                .map(solicitudECheqMapper::toResponse)
                .toList();
    }

    public List<SolicitudECheqResponse> filtrarMisSolicitudes(
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            EstadoSolicitud estado,
            String concepto) {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (!esCliente(usuario)) {
            throw new SecurityException(
                    "Esta operación está disponible únicamente para clientes"
            );
        }

        validarRangoFechas(
                fechaDesde,
                fechaHasta
        );

        LocalDateTime desde =
                fechaDesde != null
                        ? fechaDesde.atStartOfDay()
                        : null;

        LocalDateTime hasta =
                fechaHasta != null
                        ? fechaHasta.plusDays(1).atStartOfDay()
                        : null;

        String conceptoNormalizado =
                concepto != null && !concepto.isBlank()
                        ? concepto.trim()
                        : null;

        return solicitudRepository
                .filtrar(
                        usuario.getId(),
                        desde,
                        hasta,
                        estado,
                        conceptoNormalizado
                )
                .stream()
                .map(solicitudECheqMapper::toResponse)
                .toList();
    }

    private void validarRangoFechas(
            LocalDate fechaDesde,
            LocalDate fechaHasta) {

        if (fechaDesde != null
                && fechaHasta != null
                && fechaHasta.isBefore(fechaDesde)) {

            throw new IllegalArgumentException(
                    "La fecha hasta no puede ser anterior a la fecha desde"
            );
        }
    }
    public byte[] exportarSolicitudes(
            Long usuarioId,
            LocalDate fechaDesde,
            LocalDate fechaHasta,
            EstadoSolicitud estado,
            String concepto) {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (!esAdministrador(usuario)
                && !esOperador(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para exportar solicitudes"
            );
        }

        List<SolicitudECheqResponse> solicitudes =
                filtrarSolicitudes(
                        usuarioId,
                        fechaDesde,
                        fechaHasta,
                        estado,
                        concepto
                );

        return solicitudECheqExcelService.generar(
                solicitudes
        );
    }

    public SolicitudECheqResponse obtenerPorId(Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        SolicitudECheq solicitud =
                buscarSolicitud(id);

        if (esCliente(usuario)
                && !solicitud.getUsuario()
                .getId()
                .equals(usuario.getId())) {

            throw new SecurityException(
                    "No tiene permisos para consultar esta solicitud"
            );
        }

        if (!esCliente(usuario)
                && !esAdministrador(usuario)
                && !esOperador(usuario)
                && !esAuditor(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para consultar esta solicitud"
            );
        }

        return solicitudECheqMapper.toResponse(
                solicitud
        );
    }

    @Transactional
    public SolicitudECheqResponse crear(
            CrearSolicitudECheqRequest request) {

        Usuario usuarioAutenticado =
                obtenerUsuarioAutenticado();

        Usuario usuarioSolicitante;

        if (esCliente(usuarioAutenticado)) {

            if (request.getUsuarioId() == null
                    || !request.getUsuarioId()
                    .equals(usuarioAutenticado.getId())) {

                throw new SecurityException(
                        "Un cliente solamente puede crear solicitudes para sí mismo"
                );
            }

            usuarioSolicitante =
                    usuarioAutenticado;

        } else if (esAdministrador(usuarioAutenticado)
                || esOperador(usuarioAutenticado)) {

            usuarioSolicitante =
                    usuarioRepository
                            .findById(request.getUsuarioId())
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Usuario no encontrado con id: "
                                                    + request.getUsuarioId()
                                    )
                            );

            if (!esCliente(usuarioSolicitante)) {

                throw new BusinessException(
                        "Las solicitudes eCheq solamente pueden crearse para usuarios CLIENTE"
                );
            }

        } else {

            throw new SecurityException(
                    "El usuario no tiene permisos para crear solicitudes"
            );
        }

        CuentaCorriente cuentaCorriente =
                buscarCuentaCorriente(
                        request.getCuentaCorrienteId()
                );

        validarCuentaCorrienteDelSolicitante(
                cuentaCorriente,
                usuarioSolicitante,
                usuarioAutenticado
        );

        validarMonto(
                request.getMonto()
        );

        SolicitudECheq solicitud =
                solicitudECheqMapper.toEntity(
                        request,
                        usuarioSolicitante,
                        cuentaCorriente
                );

        solicitud =
                solicitudRepository.save(
                        solicitud
                );

        auditoriaService.registrar(
                AccionAuditoria.CREAR,
                "Solicitud eCheq #" + solicitud.getId() + " creada",
                usuarioAutenticado
        );
        return solicitudECheqMapper.toResponse(
                solicitud
        );
    }

    @Transactional
    public SolicitudECheqResponse actualizar(
            Long id,
            ActualizarSolicitudECheqRequest request) {

        Usuario usuarioAutenticado =
                obtenerUsuarioAutenticado();

        SolicitudECheq solicitud =
                buscarSolicitud(id);

        if (!esAdministrador(usuarioAutenticado)
                && !esOperador(usuarioAutenticado)
                && !esCliente(usuarioAutenticado)) {

            throw new SecurityException(
                    "No tiene permisos para editar solicitudes"
            );
        }

        if (esCliente(usuarioAutenticado)
                && !solicitud.getUsuario()
                .getId()
                .equals(usuarioAutenticado.getId())) {

            throw new SecurityException(
                    "No puede editar una solicitud de otro usuario"
            );
        }

        if (solicitud.getEstado()
                != EstadoSolicitud.PENDIENTE) {

            throw new IllegalStateException(
                    "Solamente se pueden editar solicitudes pendientes"
            );
        }

        CuentaCorriente cuentaCorriente =
                buscarCuentaCorriente(
                        request.getCuentaCorrienteId()
                );

        validarCuentaCorrienteDelSolicitante(
                cuentaCorriente,
                solicitud.getUsuario(),
                usuarioAutenticado
        );

        validarMonto(
                request.getMonto()
        );

        solicitudECheqMapper.updateEntity(
                solicitud,
                request
        );

        solicitud.setCuentaCorriente(
                cuentaCorriente
        );

        solicitud =
                solicitudRepository.save(
                        solicitud
                );

        auditoriaService.registrar(
                AccionAuditoria.MODIFICAR,
                "Solicitud eCheq #" + solicitud.getId() + " modificada",
                usuarioAutenticado
        );
        return solicitudECheqMapper.toResponse(
                solicitud
        );
    }

    @Transactional
    public SolicitudECheqResponse actualizarEstado(
            Long id,
            CambiarEstadoSolicitudECheqRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(usuario)
                && !esOperador(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para modificar el estado de una solicitud"
            );
        }

        SolicitudECheq solicitud =
                buscarSolicitud(id);

        EstadoSolicitud estadoActual =
                solicitud.getEstado();

        EstadoSolicitud nuevoEstado =
                request.getEstado();

        if (nuevoEstado == null) {

            throw new IllegalArgumentException(
                    "El nuevo estado es obligatorio"
            );
        }

        if (estadoActual == EstadoSolicitud.APROBADA
                || estadoActual == EstadoSolicitud.RECHAZADA) {

            throw new IllegalStateException(
                    "No se puede modificar una solicitud que ya fue aprobada o rechazada"
            );
        }

        if (estadoActual != EstadoSolicitud.PENDIENTE) {

            throw new IllegalStateException(
                    "La solicitud no se encuentra en estado pendiente"
            );
        }

        if (nuevoEstado != EstadoSolicitud.APROBADA
                && nuevoEstado != EstadoSolicitud.RECHAZADA) {

            throw new IllegalArgumentException(
                    "El estado solamente puede ser APROBADA o RECHAZADA"
            );
        }

        if (aprobacionRepository.existsBySolicitud_Id(id)) {

            throw new IllegalStateException(
                    "La solicitud ya posee una decisión registrada"
            );
        }

        solicitudECheqMapper.updateEstado(
                solicitud,
                request
        );

        solicitud =
                solicitudRepository.save(
                        solicitud
                );

        Aprobacion aprobacion =
                new Aprobacion();

        aprobacion.setSolicitud(
                solicitud
        );

        aprobacion.setUsuario(
                usuario
        );

        aprobacion.setDecision(
                nuevoEstado == EstadoSolicitud.APROBADA
                        ? DecisionAprobacion.APROBADO
                        : DecisionAprobacion.RECHAZADO
        );

        aprobacion.setFechaDecision(
                LocalDateTime.now()
        );

        if (request.getObservacion() != null
                && !request.getObservacion().isBlank()) {

            aprobacion.setObservacion(
                    request.getObservacion().trim()
            );
        }

        aprobacionRepository.save(
                aprobacion
        );

        notificacionService.crearPorCambioEstado(
                solicitud,
                nuevoEstado
        );

        auditoriaService.registrar(
                nuevoEstado == EstadoSolicitud.APROBADA
                        ? AccionAuditoria.APROBAR
                        : AccionAuditoria.RECHAZAR,
                "Solicitud eCheq #"
                        + solicitud.getId()
                        + (nuevoEstado == EstadoSolicitud.APROBADA
                        ? " aprobada"
                        : " rechazada"),
                usuario
        );
        return solicitudECheqMapper.toResponse(
                solicitud
        );
    }

    @Transactional
    public void eliminar(Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(usuario)
                && !esOperador(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para eliminar solicitudes"
            );
        }

        SolicitudECheq solicitud =
                buscarSolicitud(id);

        if (solicitud.getEstado()
                != EstadoSolicitud.PENDIENTE) {

            throw new IllegalStateException(
                    "Solamente se pueden eliminar solicitudes pendientes"
            );
        }

        solicitudRepository.delete(
                solicitud
        );

        auditoriaService.registrar(
                AccionAuditoria.ELIMINAR,
                "Solicitud eCheq #" + solicitud.getId() + " eliminada",
                usuario
        );
    }

    private SolicitudECheq buscarSolicitud(Long id) {

        return solicitudRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Solicitud eCheq no encontrada con id: "
                                        + id
                        )
                );
    }

    private CuentaCorriente buscarCuentaCorriente(Long id) {

        return cuentaCorrienteRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cuenta corriente no encontrada con id: "
                                        + id
                        )
                );
    }

    private void validarMonto(Double monto) {

        if (monto == null
                || monto <= 0) {

            throw new IllegalArgumentException(
                    "El monto debe ser mayor a cero"
            );
        }
    }

    private void validarCuentaCorrienteDelSolicitante(
            CuentaCorriente cuentaCorriente,
            Usuario usuarioSolicitante,
            Usuario usuarioAutenticado) {

        if (cuentaCorriente.getCuentaBanco() == null
                || cuentaCorriente.getCuentaBanco()
                .getCuenta() == null
                || cuentaCorriente.getCuentaBanco()
                .getCuenta()
                .getUsuario() == null) {

            throw new BusinessException(
                    "La cuenta corriente no tiene un propietario válido"
            );
        }

        Long propietarioId =
                cuentaCorriente
                        .getCuentaBanco()
                        .getCuenta()
                        .getUsuario()
                        .getId();

        if (!propietarioId.equals(
                usuarioSolicitante.getId())) {

            if (esCliente(usuarioAutenticado)) {

                throw new SecurityException(
                        "No puede utilizar una cuenta corriente perteneciente a otro usuario"
                );
            }

            throw new BusinessException(
                    "La cuenta corriente seleccionada no pertenece al usuario solicitante"
            );
        }
    }
}