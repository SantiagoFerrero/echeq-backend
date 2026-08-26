package com.echeq.repository;

import com.echeq.entity.Aprobacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AprobacionRepository
        extends JpaRepository<Aprobacion, Long> {

    Optional<Aprobacion> findBySolicitud_Id(Long solicitudId);

    boolean existsBySolicitud_Id(Long solicitudId);
}