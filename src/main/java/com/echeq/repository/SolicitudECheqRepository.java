package com.echeq.repository;

import com.echeq.entity.SolicitudECheq;
import com.echeq.enums.EstadoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SolicitudECheqRepository
        extends JpaRepository<SolicitudECheq, Long> {

    List<SolicitudECheq> findByUsuarioId(Long usuarioId);

    @Query("""
            SELECT DISTINCT s
            FROM SolicitudECheq s
            LEFT JOIN FETCH s.usuario u
            LEFT JOIN FETCH s.cuentaCorriente cc
            LEFT JOIN FETCH cc.cuentaBanco cb
            LEFT JOIN FETCH cb.banco b
            WHERE (:usuarioId IS NULL OR u.id = :usuarioId)
              AND (:fechaDesde IS NULL OR s.fechaSolicitud >= :fechaDesde)
              AND (:fechaHasta IS NULL OR s.fechaSolicitud < :fechaHasta)
              AND (:estado IS NULL OR s.estado = :estado)
              AND (
                    :concepto IS NULL
                    OR LOWER(s.concepto)
                       LIKE LOWER(CONCAT('%', :concepto, '%'))
              )
            ORDER BY s.fechaSolicitud DESC
            """)
    List<SolicitudECheq> filtrar(
            @Param("usuarioId") Long usuarioId,
            @Param("fechaDesde") LocalDateTime fechaDesde,
            @Param("fechaHasta") LocalDateTime fechaHasta,
            @Param("estado") EstadoSolicitud estado,
            @Param("concepto") String concepto
    );
}