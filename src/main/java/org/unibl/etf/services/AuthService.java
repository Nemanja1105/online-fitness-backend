package org.unibl.etf.services;

import org.unibl.etf.models.dto.CheckDetailsDTO;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.LoginRequestDTO;
import org.unibl.etf.models.dto.RegisterRequestDTO;

public interface AuthService {
    void registerClient(RegisterRequestDTO request);
    ClientDTO loginClient(LoginRequestDTO requestDTO);
    boolean checkDetails(CheckDetailsDTO checkDetailsDTO);
}
