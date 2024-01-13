package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.models.entities.ClientEntity;

public interface AuthService {
    void registerClient(RegisterRequestDTO request);
    ClientDTO loginClient(LoginRequestDTO requestDTO);
    boolean checkDetails(CheckDetailsDTO checkDetailsDTO);
    boolean activateAccount(ValidationDTO validationDTO);
    void resendActivation(ClientEntity client);
    void changePassword(ChangePasswordDTO changePasswordDTO, Authentication authentication);
}
