package com.logicsoftware.address;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;

public class AddressMocks {

    public ViaCepResponseDTO addressByCepMock() {
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
