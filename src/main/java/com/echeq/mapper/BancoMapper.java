package com.echeq.mapper;

import com.echeq.dto.request.banco.CrearBancoRequest;
import com.echeq.dto.request.banco.ActualizarBancoRequest;
import com.echeq.dto.response.banco.BancoResponse;
import com.echeq.entity.Banco;
import org.springframework.stereotype.Component;

@Component
public class BancoMapper {

    public Banco toEntity(CrearBancoRequest request) {
        Banco banco = new Banco();
        banco.setNombre(request.getNombre());
        banco.setCodigoBanco(request.getCodigoBanco());
        return banco;
    }

    public void updateEntity(Banco banco, ActualizarBancoRequest request) {
        banco.setNombre(request.getNombre());
        banco.setCodigoBanco(request.getCodigoBanco());
    }

    public BancoResponse toResponse(Banco banco) {
        BancoResponse response = new BancoResponse();
        response.setId(banco.getId());
        response.setNombre(banco.getNombre());
        response.setCodigoBanco(banco.getCodigoBanco());
        return response;
    }
}