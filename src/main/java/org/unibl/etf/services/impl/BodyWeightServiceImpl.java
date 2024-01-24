package org.unibl.etf.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.models.entities.BodyWeightEntity;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.repositories.BodyWeightRepository;
import org.unibl.etf.services.BodyWeightService;
import org.unibl.etf.services.LogService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class BodyWeightServiceImpl implements BodyWeightService {

    private final BodyWeightRepository bodyWeightRepository;
    private final ModelMapper mapper;
    private final LogService logService;

    public BodyWeightServiceImpl(BodyWeightRepository bodyWeightRepository, ModelMapper mapper, LogService logService) {
        this.bodyWeightRepository = bodyWeightRepository;
        this.mapper = mapper;
        this.logService = logService;
    }

    @Override
    public BodyWeightDTO insertBodyWeightForClient(Long clientId, BodyWeightRequestDTO requestDTO, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var entity=mapper.map(requestDTO, BodyWeightEntity.class);
        entity.setId(null);
       // entity.setCreatedAt(new Date());
        ClientEntity client=new ClientEntity(); client.setId(clientId);
        entity.setClient(client);
        entity=this.bodyWeightRepository.saveAndFlush(entity);
        this.logService.info("Client "+jwtUser.getUsername()+" successfully added new bodyweight.");
        return mapper.map(entity,BodyWeightDTO.class);
    }

    @Override
    public BodyWeightStatisticDTO findStatisticForClient(Long clientId, BodyWeightFilterDTO request, Authentication authentication) {
        var jwtUser = (JwtUserDTO) authentication.getPrincipal();
        if (!jwtUser.getId().equals(clientId)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        List<BodyWeightEntity> result;

        if(request.getStartDate()!=null && request.getEndDate()!=null)
            result=this.bodyWeightRepository.findAllByClientIdAndCreatedAtBetweenOrderByCreatedAtAsc(clientId,request.getStartDate(),getEndOfDay(request.getEndDate()));
        else if(request.getStartDate()!=null)
            result=this.bodyWeightRepository.findAllByClientIdAndCreatedAtAfterOrderByCreatedAtAsc(clientId,request.getStartDate());
        else if(request.getEndDate()!=null)
            result=this.bodyWeightRepository.findAllByClientIdAndCreatedAtBeforeOrderByCreatedAtAsc(clientId,getEndOfDay(request.getEndDate()));
        else
            result=this.bodyWeightRepository.findAllByClientIdOrderByCreatedAtAsc(clientId);
        SimpleDateFormat formatter=new SimpleDateFormat("dd.MM.yyyy");
        var xvalues=result.stream().map(BodyWeightEntity::getCreatedAt).map(formatter::format).toList();
        var yvalues=result.stream().map(BodyWeightEntity::getWeight).toList();
        return new BodyWeightStatisticDTO(xvalues,yvalues);

    }

    private Date getEndOfDay(Date endDate){
        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTime(endDate);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);
        return  endOfDay.getTime();
    }
}
