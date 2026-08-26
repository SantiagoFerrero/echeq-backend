package com.echeq.repository;

import com.echeq.entity.CuentaCorriente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaCorrienteRepository
        extends JpaRepository<CuentaCorriente, Long> {

    Optional<CuentaCorriente> findByCbu(String cbu);

    Optional<CuentaCorriente> findByAlias(String alias);

    boolean existsByCbu(String cbu);

    boolean existsByAlias(String alias);

    boolean existsByCbuAndIdNot(String cbu, Long id);

    boolean existsByAliasAndIdNot(String alias, Long id);

    List<CuentaCorriente>
    findByCuentaBanco_Cuenta_Usuario_Id(Long usuarioId);
}