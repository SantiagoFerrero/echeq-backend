package com.echeq.repository;

import com.echeq.entity.CuentaBanco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuentaBancoRepository extends JpaRepository<CuentaBanco, Long> {

    boolean existsByBancoId(Long bancoId);

    boolean existsByCuentaIdAndBancoId(Long cuentaId, Long bancoId);

    List<CuentaBanco> findByCuenta_Usuario_Id(Long usuarioId);
}