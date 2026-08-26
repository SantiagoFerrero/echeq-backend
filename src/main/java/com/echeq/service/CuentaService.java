package com.echeq.service;

import com.echeq.dto.request.cuenta.ActualizarCuentaRequest;
import com.echeq.dto.request.cuenta.CrearCuentaRequest;
import com.echeq.dto.request.cuenta.CrearMiCuentaRequest;
import com.echeq.dto.response.cuenta.CuentaResponse;
import com.echeq.entity.Banco;
import com.echeq.entity.Cuenta;
import com.echeq.entity.Usuario;
import com.echeq.enums.NombreRol;
import com.echeq.exception.BusinessException;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.CuentaMapper;
import com.echeq.repository.BancoRepository;
import com.echeq.repository.CuentaRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final BancoRepository bancoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaMapper cuentaMapper;

    public CuentaService(
            CuentaRepository cuentaRepository,
            BancoRepository bancoRepository,
            UsuarioRepository usuarioRepository,
            CuentaMapper cuentaMapper) {

        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaMapper = cuentaMapper;
    }

    // =========================================================
    // USUARIO AUTENTICADO
    // =========================================================

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

    // =========================================================
    // ROLES
    // =========================================================

    private boolean esCliente(Usuario usuario) {
        return usuario.getRol() != null
                && usuario.getRol().getNombre() == NombreRol.CLIENTE;
    }

    private boolean esAdministrador(Usuario usuario) {
        return usuario.getRol() != null
                && usuario.getRol().getNombre() == NombreRol.ADMIN;
    }

    private boolean esOperador(Usuario usuario) {
        return usuario.getRol() != null
                && usuario.getRol().getNombre() == NombreRol.OPERADOR;
    }

    private boolean esAuditor(Usuario usuario) {
        return usuario.getRol() != null
                && usuario.getRol().getNombre() == NombreRol.AUDITOR;
    }

    // =========================================================
    // TODAS LAS CUENTAS
    // ADMIN / OPERADOR / AUDITOR
    // =========================================================

    @Transactional
    public List<CuentaResponse> obtenerTodas() {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(autenticado)
                && !esOperador(autenticado)
                && !esAuditor(autenticado)) {

            throw new SecurityException(
                    "No tiene permisos para consultar todas las cuentas"
            );
        }

        return cuentaRepository
                .findAll()
                .stream()
                .map(cuentaMapper::toResponse)
                .toList();
    }

    // =========================================================
    // MIS CUENTAS
    // CLIENTE
    // =========================================================

    @Transactional
    public List<CuentaResponse> obtenerMisCuentas() {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esCliente(autenticado)) {

            throw new SecurityException(
                    "Esta operación solamente está disponible para clientes"
            );
        }

        return cuentaRepository
                .findByUsuarioId(autenticado.getId())
                .stream()
                .map(cuentaMapper::toResponse)
                .toList();
    }

    // =========================================================
    // CUENTA POR ID
    // =========================================================

    @Transactional
    public CuentaResponse obtenerPorId(Long id) {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        Cuenta cuenta =
                buscarCuenta(id);

        if (esCliente(autenticado)) {

            if (cuenta.getUsuario() == null
                    || !cuenta.getUsuario()
                    .getId()
                    .equals(autenticado.getId())) {

                throw new SecurityException(
                        "No tiene permisos para consultar esta cuenta"
                );
            }

        } else if (!esAdministrador(autenticado)
                && !esOperador(autenticado)
                && !esAuditor(autenticado)) {

            throw new SecurityException(
                    "No tiene permisos para consultar esta cuenta"
            );
        }

        return cuentaMapper.toResponse(cuenta);
    }

    // =========================================================
    // CREAR
    // ADMIN / OPERADOR
    // =========================================================

    @Transactional
    public CuentaResponse crear(
            CrearCuentaRequest request) {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(autenticado)
                && !esOperador(autenticado)) {

            throw new SecurityException(
                    "No tiene permisos para crear cuentas"
            );
        }

        String numeroCuenta =
                request.getNumeroCuenta().trim();

        if (cuentaRepository
                .existsByNumeroCuenta(numeroCuenta)) {

            throw new BusinessException(
                    "Ya existe una cuenta con ese número"
            );
        }

        Banco banco =
                bancoRepository
                        .findById(request.getBancoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Banco no encontrado con id: "
                                                + request.getBancoId()
                                )
                        );

        Usuario propietario =
                usuarioRepository
                        .findById(request.getUsuarioId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado con id: "
                                                + request.getUsuarioId()
                                )
                        );

        if (!esCliente(propietario)) {

            throw new IllegalArgumentException(
                    "La cuenta debe pertenecer a un usuario con rol CLIENTE"
            );
        }

        Cuenta cuenta =
                cuentaMapper.toEntity(request);

        cuenta.setBanco(banco);
        cuenta.setUsuario(propietario);

        cuenta = cuentaRepository.save(cuenta);

        return cuentaMapper.toResponse(cuenta);
    }

    // =========================================================
    // ACTUALIZAR
    // ADMIN / OPERADOR
    // =========================================================


    // =========================================================
    // CREAR MI CUENTA
    // CLIENTE
    // =========================================================

    @Transactional
    public CuentaResponse crearMiCuenta(
            CrearMiCuentaRequest request) {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esCliente(autenticado)) {

            throw new SecurityException(
                    "Esta operación solamente está disponible para clientes"
            );
        }

        String numeroCuenta =
                request.getNumeroCuenta().trim();

        if (cuentaRepository
                .existsByNumeroCuenta(numeroCuenta)) {

            throw new BusinessException(
                    "Ya existe una cuenta con ese número"
            );
        }

        Banco banco =
                bancoRepository
                        .findById(request.getBancoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Banco no encontrado con id: "
                                                + request.getBancoId()
                                )
                        );

        Cuenta cuenta = new Cuenta();

        cuenta.setNumeroCuenta(numeroCuenta);
        cuenta.setSaldo(request.getSaldo());
        cuenta.setBanco(banco);

        // El propietario siempre es el usuario autenticado.
        cuenta.setUsuario(autenticado);

        cuenta = cuentaRepository.save(cuenta);

        return cuentaMapper.toResponse(cuenta);
    }
    @Transactional
    public CuentaResponse actualizar(
            Long id,
            ActualizarCuentaRequest request) {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(autenticado)
                && !esOperador(autenticado)) {

            throw new SecurityException(
                    "No tiene permisos para modificar cuentas"
            );
        }

        Cuenta cuenta =
                buscarCuenta(id);

        String nuevoNumero =
                request.getNumeroCuenta().trim();

        if (cuentaRepository
                .existsByNumeroCuentaAndIdNot(
                        nuevoNumero,
                        id
                )) {

            throw new BusinessException(
                    "Ya existe una cuenta con ese número"
            );
        }

        Banco banco =
                bancoRepository
                        .findById(request.getBancoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Banco no encontrado con id: "
                                                + request.getBancoId()
                                )
                        );

        Usuario propietario =
                usuarioRepository
                        .findById(request.getUsuarioId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Usuario no encontrado con id: "
                                                + request.getUsuarioId()
                                )
                        );

        if (!esCliente(propietario)) {

            throw new IllegalArgumentException(
                    "La cuenta debe pertenecer a un usuario con rol CLIENTE"
            );
        }

        cuentaMapper.updateEntity(
                cuenta,
                request
        );

        cuenta.setBanco(banco);
        cuenta.setUsuario(propietario);

        cuenta =
                cuentaRepository.save(cuenta);

        return cuentaMapper.toResponse(cuenta);
    }

    // =========================================================
    // ELIMINAR
    // ADMIN / OPERADOR
    // =========================================================

    @Transactional
    public void eliminar(Long id) {

        Usuario autenticado =
                obtenerUsuarioAutenticado();

        if (!esAdministrador(autenticado)
                && !esOperador(autenticado)) {

            throw new SecurityException(
                    "No tiene permisos para eliminar cuentas"
            );
        }

        Cuenta cuenta =
                buscarCuenta(id);

        cuentaRepository.delete(cuenta);
    }

    // =========================================================
    // BUSCAR CUENTA
    // =========================================================

    private Cuenta buscarCuenta(Long id) {

        return cuentaRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cuenta no encontrada con id: " + id
                        )
                );
    }
}