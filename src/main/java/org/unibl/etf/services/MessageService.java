package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.MessageDTO;
import org.unibl.etf.models.dto.MessageRequestDTO;

public interface MessageService {
    MessageDTO sendMessage(MessageRequestDTO requestDTO, Authentication auth);
}
