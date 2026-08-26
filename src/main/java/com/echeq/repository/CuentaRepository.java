package com.echeq.repository;

import com.echeq.entity.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    boolean existsByNumeroCuenta(String numeroCuenta);

    boolean existsByNumeroCuentaAndIdNot(
            String numeroCuenta,
            Long id
    );

    boolean existsByBancoId(Long bancoId);

    List<Cuenta> findByUsuarioId(Long usuarioId);
}