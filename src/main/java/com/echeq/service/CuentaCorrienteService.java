package com.echeq.service;

import com.echeq.dto.request.cuentaCorriente.ActualizarCuentaCorrienteRequest;
import com.echeq.dto.request.cuentaCorriente.CrearCuentaCorrienteRequest;
import com.echeq.dto.response.cuentaCorriente.CuentaCorrienteResponse;
import com.echeq.entity.CuentaBanco;
import com.echeq.entity.CuentaCorriente;
import com.echeq.entity.Usuario;
import com.echeq.enums.NombreRol;
import com.echeq.exception.BusinessException;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.CuentaCorrienteMapper;
import com.echeq.repository.CuentaBancoRepository;
import com.echeq.repository.CuentaCorrienteRepository;
import com.echeq.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CuentaCorrienteService {

    private final CuentaCorrienteRepository cuentaCorrienteRepository;
    private final CuentaBancoRepository cuentaBancoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CuentaCorrienteMapper cuentaCorrienteMapper;

    public CuentaCorrienteService(
            CuentaCorrienteRepository cuentaCorrienteRepository,
            CuentaBancoRepository cuentaBancoRepository,
            UsuarioRepository usuarioRepository,
            CuentaCorrienteMapper cuentaCorrienteMapper) {

        this.cuentaCorrienteRepository = cuentaCorrienteRepository;
        this.cuentaBancoRepository = cuentaBancoRepository;
        this.usuarioRepository = usuarioRepository;
        this.cuentaCorrienteMapper = cuentaCorrienteMapper;
    }

    @Transactional
    public List<CuentaCorrienteResponse> obtenerTodas() {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!puedeVerTodas(usuario)) {
            throw new SecurityException(
                    "No tiene permisos para consultar todas las cuentas corrientes."
            );
        }

        return cuentaCorrienteRepository.findAll()
                .stream()
                .map(cuentaCorrienteMapper::toResponse)
                .toList();
    }

    @Transactional
    public List<CuentaCorrienteResponse> obtenerMisCuentasCorrientes() {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!tieneRol(usuario, NombreRol.CLIENTE)) {
            throw new SecurityException(
                    "Esta operación está disponible únicamente para clientes."
            );
        }

        return cuentaCorrienteRepository
                .findByCuentaBanco_Cuenta_Usuario_Id(
                        usuario.getId()
                )
                .stream()
                .map(cuentaCorrienteMapper::toResponse)
                .toList();
    }

    @Transactional
    public CuentaCorrienteResponse obtenerPorId(Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        CuentaCorriente cuentaCorriente =
                buscarCuentaCorriente(id);

        if (tieneRol(usuario, NombreRol.CLIENTE)) {

            Long propietarioId =
                    cuentaCorriente
                            .getCuentaBanco()
                            .getCuenta()
                            .getUsuario()
                            .getId();

            if (!propietarioId.equals(usuario.getId())) {
                throw new SecurityException(
                        "No tiene permisos para consultar esta cuenta corriente."
                );
            }

        } else if (!puedeVerTodas(usuario)) {

            throw new SecurityException(
                    "No tiene permisos para consultar esta cuenta corriente."
            );
        }

        return cuentaCorrienteMapper.toResponse(
                cuentaCorriente
        );
    }

    @Transactional
    public CuentaCorrienteResponse crear(
            CrearCuentaCorrienteRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        String cbu =
                request.getCbu().trim();

        String alias =
                request.getAlias().trim();

        if (cuentaCorrienteRepository.existsByCbu(cbu)) {

            throw new BusinessException(
                    "Ya existe una cuenta corriente con ese CBU."
            );
        }

        if (cuentaCorrienteRepository.existsByAlias(alias)) {

            throw new BusinessException(
                    "Ya existe una cuenta corriente con ese alias."
            );
        }

        CuentaBanco cuentaBanco =
                cuentaBancoRepository.findById(
                        request.getCuentaBancoId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "CuentaBanco no encontrada con id: "
                                        + request.getCuentaBancoId()
                        )
                );

        CuentaCorriente cuentaCorriente =
                cuentaCorrienteMapper.toEntity(
                        request,
                        cuentaBanco
                );

        cuentaCorriente =
                cuentaCorrienteRepository.save(
                        cuentaCorriente
                );

        return cuentaCorrienteMapper.toResponse(
                cuentaCorriente
        );
    }


    // =========================================================
    // CREAR MI CUENTA CORRIENTE
    // CLIENTE
    // =========================================================

    @Transactional
    public CuentaCorrienteResponse crearMiCuentaCorriente(
            CrearCuentaCorrienteRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        if (!tieneRol(usuario, NombreRol.CLIENTE)) {
            throw new SecurityException(
                    "Esta operación solamente está disponible para clientes."
            );
        }

        String cbu =
                request.getCbu().trim();

        String alias =
                request.getAlias().trim();

        if (cuentaCorrienteRepository.existsByCbu(cbu)) {
            throw new BusinessException(
                    "Ya existe una cuenta corriente con ese CBU."
            );
        }

        if (cuentaCorrienteRepository.existsByAlias(alias)) {
            throw new BusinessException(
                    "Ya existe una cuenta corriente con ese alias."
            );
        }

        CuentaBanco cuentaBanco =
                cuentaBancoRepository
                        .findById(request.getCuentaBancoId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Cuenta Banco no encontrada con id: "
                                                + request.getCuentaBancoId()
                                )
                        );

        if (cuentaBanco.getCuenta() == null
                || cuentaBanco.getCuenta().getUsuario() == null
                || !cuentaBanco.getCuenta()
                        .getUsuario()
                        .getId()
                        .equals(usuario.getId())) {

            throw new SecurityException(
                    "No puede crear una cuenta corriente sobre una Cuenta Banco que no le pertenece."
            );
        }

        CuentaCorriente cuentaCorriente =
                cuentaCorrienteMapper.toEntity(
                        request,
                        cuentaBanco
                );

        cuentaCorriente =
                cuentaCorrienteRepository.save(
                        cuentaCorriente
                );

        return cuentaCorrienteMapper.toResponse(
                cuentaCorriente
        );
    }
    @Transactional
    public CuentaCorrienteResponse actualizar(
            Long id,
            ActualizarCuentaCorrienteRequest request) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        CuentaCorriente cuentaCorriente =
                buscarCuentaCorriente(id);

        String nuevoCbu =
                request.getCbu().trim();

        String nuevoAlias =
                request.getAlias().trim();

        if (cuentaCorrienteRepository
                .existsByCbuAndIdNot(nuevoCbu, id)) {

            throw new BusinessException(
                    "Ya existe otra cuenta corriente con ese CBU."
            );
        }

        if (cuentaCorrienteRepository
                .existsByAliasAndIdNot(nuevoAlias, id)) {

            throw new BusinessException(
                    "Ya existe otra cuenta corriente con ese alias."
            );
        }

        cuentaCorrienteMapper.updateEntity(
                cuentaCorriente,
                request
        );

        cuentaCorriente =
                cuentaCorrienteRepository.save(
                        cuentaCorriente
                );

        return cuentaCorrienteMapper.toResponse(
                cuentaCorriente
        );
    }

    @Transactional
    public void eliminar(Long id) {

        Usuario usuario =
                obtenerUsuarioAutenticado();

        validarPuedeOperar(usuario);

        CuentaCorriente cuentaCorriente =
                buscarCuentaCorriente(id);

        cuentaCorrienteRepository.delete(
                cuentaCorriente
        );
    }

    private CuentaCorriente buscarCuentaCorriente(Long id) {

        return cuentaCorrienteRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cuenta corriente no encontrada con id: "
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

        String email =
                authentication.getName();

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