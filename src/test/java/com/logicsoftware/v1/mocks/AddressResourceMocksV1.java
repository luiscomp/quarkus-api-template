package com.logicsoftware.v1.mocks;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;

public class AddressResourceMocksV1 {

    public static ViaCepResponseDTO viaCepAddress() {
        return ViaCepResponseDTO.builder()
                .cep("64033660")
                .logradouro("Rua 1")
                .complemento("casa 1")
                .bairro("Centro")
                .localidade("Teresina")
                .uf("PI")
                .ibge("2211001")
                .gia("1004")
                .ddd("86")
                .siafi("7107")
                .build();
    }
}
