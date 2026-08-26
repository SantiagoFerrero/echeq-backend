package com.echeq.service;

import com.echeq.dto.request.cuentaBanco.ActualizarCuentaBancoRequest;
import com.echeq.dto.request.cuentaBanco.CrearCuentaBancoRequest;
import com.echeq.dto.response.cuentaBanco.CuentaBancoResponse;
import com.echeq.entity.Banco;
import com.echeq.entity.Cuenta;
import com.echeq.entity.CuentaBanco;
import com.echeq.entity.Usuario;
import com.echeq.enums.NombreRol;
import com.echeq.exception.BusinessException;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.CuentaBancoMapper;
import com.echeq.repository.BancoRepository;
import com.echeq.repository.CuentaBancoRepository;
import com.echeq.repository.CuentaRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaBancoService {

    private final CuentaBancoRepository cuentaBancoRepository;
    private final CuentaRepository cuentaRepository;
    private final BancoRepository bancoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaBancoMapper cuentaBancoMapper;

    public CuentaBancoService(
            CuentaBancoRepository cuentaBancoRepository,
            CuentaRepository cuentaRepository,
            BancoRepository bancoRepository,
            UsuarioRepository usuarioRepository,
            CuentaBancoMapper cuentaBancoMapper) {

        this.cuentaBancoRepository = cuentaBancoRepository;
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaBancoMapper = cuentaBancoMapper;
    }

    @Transactional
    public List<CuentaBancoResponse> obtenerTodas() {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (!puedeVerTodas(usuario)) {
            throw new SecurityException(
                    "No tiene permisos para consultar todas las cuentas bancarias."
            );
        }

        return cuentaBancoRepository.findAll()
                .stream()
                .map(cuentaBancoMapper::toResponse)
                .toList();
    }

    @Transactional
    public List<CuentaBancoResponse> obtenerMisCuentasBanco() {

        Usuario usuario = obtenerUsuarioAutenticado();

        if (!tieneRol(usuario, NombreRol.CLIENTE)) {
            throw new SecurityException(
                    "Esta operación está disponible únicamente para clientes."
            );
        }

        return cuentaBancoRepository
                .findByCuenta_Usuario_Id(usuario.getId())
                .stream()
                .map(cuentaBancoMapper::toResponse)
                .toList();
    }

    @Transactional
    public CuentaBancoResponse obtenerPorId(Long id) {

        Usuario usuario = obtenerUsuarioAutenticado();

        CuentaBanco cuentaBanco =
                buscarCuentaBanco(id);

        if (tieneRol(usuario, NombreRol.CLIENTE)) {

            Long propietarioId =
                    cuentaBanco.getCuenta()
                            .getUsuario()
                            .getId();

            if (!propietarioId.equals(usuario.getId())) {
                throw new SecurityException(
                        "No tiene permisos para consultar esta cuenta bancaria."
                );
            }

        } else if (!puedeVerTodas(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para consultar esta cuenta bancaria."
            );
        }

        return cuentaBancoMapper.toResponse(cuentaBanco);
    }

    @Transactional
    public CuentaBancoResponse crear(
            CrearCuentaBancoRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        Cuenta cuenta = cuentaRepository.findById(
                request.getCuentaId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Cuenta no encontrada con id: "
                                + request.getCuentaId()
                )
        );

        Banco banco = bancoRepository.findById(
                request.getBancoId()
        ).orElseThrow(() ->
                new ResourceNotFoundException(
                        "Banco no encontrado con id: "
                                + request.getBancoId()
                )
        );

        if (cuentaBancoRepository.existsByCuentaIdAndBancoId(
                request.getCuentaId(),
                request.getBancoId()
        )) {

            throw new BusinessException(
                    "La cuenta ya está asociada a ese banco."
            );
        }

        CuentaBanco cuentaBanco =
                cuentaBancoMapper.toEntity(
                        cuenta,
                        banco
                );

        cuentaBanco =
                cuentaBancoRepository.save(cuentaBanco);

        return cuentaBancoMapper.toResponse(cuentaBanco);
    }


    // =========================================================
    // CREAR MI CUENTA BANCO
    // CLIENTE
    // =========================================================

    @Transactional
    public CuentaBancoResponse crearMiCuentaBanco(
            CrearCuentaBancoRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!tieneRol(usuario, NombreRol.CLIENTE)) {
            throw new SecurityException(
                    "Esta operación solamente está disponible para clientes."
            );
        }

        Cuenta cuenta =
                cuentaRepository
                        .findById(request.getCuentaId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cuenta no encontrada con id: "
                                                + request.getCuentaId()
                                )
                        );

        if (cuenta.getUsuario() == null
                || !cuenta.getUsuario()
                        .getId()
                        .equals(usuario.getId())) {

            throw new SecurityException(
                    "No puede asociar una cuenta que no le pertenece."
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

        if (cuentaBancoRepository
                .existsByCuentaIdAndBancoId(
                        request.getCuentaId(),
                        request.getBancoId()
                )) {

            throw new BusinessException(
                    "La cuenta ya está asociada a ese banco."
            );
        }

        CuentaBanco cuentaBanco =
                cuentaBancoMapper.toEntity(
                        cuenta,
                        banco
                );

        cuentaBanco =
                cuentaBancoRepository.save(
                        cuentaBanco
                );

        return cuentaBancoMapper
                .toResponse(cuentaBanco);
    }
    @Transactional
    public CuentaBancoResponse actualizar(
            Long id,
            ActualizarCuentaBancoRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        CuentaBanco cuentaBanco =
                buscarCuentaBanco(id);

        cuentaBancoMapper.updateEntity(
                cuentaBanco,
                request
        );

        cuentaBanco =
                cuentaBancoRepository.save(cuentaBanco);

        return cuentaBancoMapper.toResponse(cuentaBanco);
    }

    @Transactional
    public void eliminar(Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        CuentaBanco cuentaBanco =
                buscarCuentaBanco(id);

        cuentaBancoRepository.delete(cuentaBanco);
    }

    private CuentaBanco buscarCuentaBanco(Long id) {

        return cuentaBancoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CuentaBanco no encontrada con id: "
                                        + id
                        )
                );
    }

    private Usuario obtenerUsuarioAutenticado() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            throw new SecurityException(
                    "Usuario no autenticado."
            );
        }

        String email = authentication.getName();

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuario autenticado no encontrado."
                        )
                );
    }

    private boolean tieneRol(
            Usuario usuario,
            NombreRol rol) {

        return usuario.getRol() != null
                && usuario.getRol().getNombre() == rol;
    }

    private boolean puedeVerTodas(
            Usuario usuario) {

        return tieneRol(usuario, NombreRol.ADMIN)
                || tieneRol(usuario, NombreRol.OPERADOR)
                || tieneRol(usuario, NombreRol.AUDITOR);
    }

    private boolean puedeOperar(
            Usuario usuario) {

        return tieneRol(usuario, NombreRol.ADMIN)
                || tieneRol(usuario, NombreRol.OPERADOR);
    }

    private void validarPuedeOperar(
            Usuario usuario) {

        if (!puedeOperar(usuario)) {
            throw new SecurityException(
                    "No tiene permisos para realizar esta operación."
            );
        }
    }
}