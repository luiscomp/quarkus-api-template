package com.logicsoftware.services;

import com.logicsoftware.dtos.address.ViaCepResponseDTO;

public interface AddressService {

    ViaCepResponseDTO getAddressByCep(String cep);

}