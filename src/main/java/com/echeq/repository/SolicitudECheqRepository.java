package com.echeq.repository;

import com.echeq.entity.SolicitudECheq;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolicitudECheqRepository
        extends JpaRepository<SolicitudECheq, Long> {

    List<SolicitudECheq> findByUsuarioId(Long usuarioId);
}