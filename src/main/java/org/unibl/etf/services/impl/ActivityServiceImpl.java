package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.ActivityDTO;
import org.unibl.etf.models.dto.ActivityRequestDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.entities.ActivityEntity;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.repositories.ActivityRepository;
import org.unibl.etf.services.ActivityService;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ModelMapper mapper;

    public ActivityServiceImpl(ActivityRepository activityRepository, ModelMapper mapper) {
        this.activityRepository = activityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<ActivityDTO> findAllActivitiesForClient(Long clientId, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId))
            throw new UnauthorizedException();
        return this.activityRepository.findAllByClientIdAndStatus(clientId,true).stream()
                .map(el->mapper.map(el,ActivityDTO.class)).toList();
    }

    @Override
    public ActivityDTO insertActivityForClient(Long clientId, ActivityRequestDTO requestDTO, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId))
            throw new UnauthorizedException();
        var entity=mapper.map(requestDTO, ActivityEntity.class);
        entity.setCreatedAt(new Date());
        entity.setStatus(true);
        ClientEntity client=new ClientEntity(); client.setId(clientId);
        entity.setClient(client);
        entity=this.activityRepository.saveAndFlush(entity);
        return mapper.map(entity,ActivityDTO.class);
    }

    @Override
    public void deleteActivity(Long clientId, Long activityId, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId))
            throw new UnauthorizedException();
        var entity=this.activityRepository.findById(activityId).orElseThrow(NotFoundException::new);
        entity.setStatus(false);
    }
}
