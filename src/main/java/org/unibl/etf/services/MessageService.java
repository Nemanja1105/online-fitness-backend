package org.unibl.etf.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.MessageDTO;
import org.unibl.etf.models.dto.MessageRequestDTO;


import java.util.List;

public interface MessageService {
    MessageDTO sendMessage(MessageRequestDTO requestDTO, Authentication auth);
    Page<MessageDTO> findAllMessageForClient(Long id, Authentication auth, Pageable page);
    void markAsRead(Long id,Authentication auth);
}
