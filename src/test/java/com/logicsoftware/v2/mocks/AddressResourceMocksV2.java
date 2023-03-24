package com.logicsoftware.v2.mocks;

import com.logicsoftware.dtos.address.ApiCepResponseDTO;

public class AddressResourceMocksV2 {

    public static ApiCepResponseDTO apiCepAddress() {
        return ApiCepResponseDTO.builder()
                .cep("64033660")
                .logradouro("Rua 1")
                .complemento("casa 1")
                .bairro("Centro")
                .localidade("Teresina")
                .uf("PI")
                .ibge("2211001")
                .build();
    }
}
