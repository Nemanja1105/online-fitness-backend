package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.AdvisorQuestionRequestDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.entities.AdvisorQuestionEntity;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.entities.FitnessProgramEntity;
import org.unibl.etf.repositories.AdvisorQuestionRepository;
import org.unibl.etf.services.AdvisorQuestionService;
import org.unibl.etf.services.LogService;

import java.util.Date;

@Service
@Transactional
public class AdvisorQuestionServiceImpl implements AdvisorQuestionService {

    private final AdvisorQuestionRepository advisorQuestionRepository;
    private final LogService logService;
    private final ModelMapper mapper;

    public AdvisorQuestionServiceImpl(AdvisorQuestionRepository advisorQuestionRepository, LogService logService, ModelMapper mapper) {
        this.advisorQuestionRepository = advisorQuestionRepository;
        this.logService = logService;
        this.mapper = mapper;
    }

    @Override
    public void sendQuestion(AdvisorQuestionRequestDTO requestDTO, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(requestDTO.getSender())) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var entity=mapper.map(requestDTO, AdvisorQuestionEntity.class);
        entity.setId(null);
        entity.setSeen(false);
        entity.setCreatedAt(new Date());
        ClientEntity client=new ClientEntity(); client.setId(requestDTO.getSender());
        entity.setSender(client);
        FitnessProgramEntity fp=new FitnessProgramEntity(); fp.setId(requestDTO.getFitnessProgram());
        entity.setFitnessProgram(fp);
        entity=this.advisorQuestionRepository.saveAndFlush(entity);
        this.logService.warning("Client "+jwtUser.getUsername()+" successfully sent a question to advisor.");
    }
}
