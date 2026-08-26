package com.echeq.service;

import com.echeq.dto.request.banco.ActualizarBancoRequest;
import com.echeq.dto.request.banco.CrearBancoRequest;
import com.echeq.dto.response.banco.BancoResponse;
import com.echeq.entity.Banco;
import com.echeq.exception.BusinessException;
import com.echeq.exception.ResourceNotFoundException;
import com.echeq.mapper.BancoMapper;
import com.echeq.repository.BancoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BancoService {

    private final BancoRepository bancoRepository;
    private final BancoMapper mapper;

    public BancoService(
            BancoRepository bancoRepository,
            BancoMapper mapper) {

        this.bancoRepository = bancoRepository;
        this.mapper = mapper;
    }

    @Transactional
    public List<BancoResponse> obtenerTodos() {

        return bancoRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional
    public BancoResponse obtenerPorId(Long id) {

        Banco banco = bancoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Banco no encontrado con id: " + id
                        )
                );

        return mapper.toResponse(banco);
    }

    @Transactional
    public BancoResponse crear(
            CrearBancoRequest request) {

        String nombre =
                request.getNombre().trim();

        String codigoBanco =
                request.getCodigoBanco().trim();

        if (bancoRepository
                .existsByNombreIgnoreCase(nombre)) {

            throw new BusinessException(
                    "Ya existe un banco con ese nombre."
            );
        }

        if (bancoRepository
                .existsByCodigoBancoIgnoreCase(codigoBanco)) {

            throw new BusinessException(
                    "Ya existe un banco con ese código."
            );
        }

        Banco banco =
                mapper.toEntity(request);

        banco.setNombre(nombre);
        banco.setCodigoBanco(codigoBanco);

        banco =
                bancoRepository.save(banco);

        return mapper.toResponse(banco);
    }

    @Transactional
    public BancoResponse actualizar(
            Long id,
            ActualizarBancoRequest request) {

        Banco banco =
                bancoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Banco no encontrado con id: " + id
                                )
                        );

        String nombre =
                request.getNombre().trim();

        String codigoBanco =
                request.getCodigoBanco().trim();

        if (bancoRepository
                .existsByNombreIgnoreCaseAndIdNot(
                        nombre,
                        id
                )) {

            throw new BusinessException(
                    "Ya existe otro banco con ese nombre."
            );
        }

        if (bancoRepository
                .existsByCodigoBancoIgnoreCaseAndIdNot(
                        codigoBanco,
                        id
                )) {

            throw new BusinessException(
                    "Ya existe otro banco con ese código."
            );
        }

        mapper.updateEntity(
                banco,
                request
        );

        banco.setNombre(nombre);
        banco.setCodigoBanco(codigoBanco);

        banco =
                bancoRepository.save(banco);

        return mapper.toResponse(banco);
    }

    @Transactional
    public void eliminar(Long id) {

        Banco banco =
                bancoRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Banco no encontrado con id: " + id
                                )
                        );

        bancoRepository.delete(banco);
    }
}