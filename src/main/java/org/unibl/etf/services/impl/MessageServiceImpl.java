package org.unibl.etf.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.dto.MessageDTO;
import org.unibl.etf.models.dto.MessageRequestDTO;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.entities.MessageEntity;
import org.unibl.etf.repositories.MessageRepository;
import org.unibl.etf.services.MessageService;

import java.util.Date;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ModelMapper mapper;

    @PersistenceContext
    private EntityManager entityManager;

    public MessageServiceImpl(MessageRepository messageRepository, ModelMapper mapper) {
        this.messageRepository = messageRepository;
        this.mapper = mapper;
    }

    @Override
    public MessageDTO sendMessage(MessageRequestDTO requestDTO, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(requestDTO.getSender()))
            throw new UnauthorizedException();
        var entity=mapper.map(requestDTO, MessageEntity.class);
        entity.setId(null);
        entity.setCreatedAt(new Date());
        entity.setSeen(false);
        ClientEntity sender=new ClientEntity(); sender.setId(requestDTO.getSender());
        entity.setSender(sender);
        ClientEntity receiver=new ClientEntity(); receiver.setId(requestDTO.getReceiver());
        entity.setReceiver(receiver);
        entity=this.messageRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        return mapper.map(entity,MessageDTO.class);
    }
}
