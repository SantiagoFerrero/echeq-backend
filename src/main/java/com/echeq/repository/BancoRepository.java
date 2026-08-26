package com.echeq.repository;

import com.echeq.entity.Banco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BancoRepository extends JpaRepository<Banco, Long> {

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByCodigoBancoIgnoreCase(String codigoBanco);

    boolean existsByNombreIgnoreCaseAndIdNot(
            String nombre,
            Long id
    );

    boolean existsByCodigoBancoIgnoreCaseAndIdNot(
            String codigoBanco,
            Long id
    );
}