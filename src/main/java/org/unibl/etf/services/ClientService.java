package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.UpdateClientDTO;

public interface ClientService {
     ClientDTO updateClient(Long id, UpdateClientDTO request, Authentication auth);
}
