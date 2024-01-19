package org.unibl.etf.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.FitnessProgramDTO;
import org.unibl.etf.models.dto.FitnessProgramRequestDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.entities.*;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.Location;
import org.unibl.etf.repositories.FitnessProgramAttributeRepository;
import org.unibl.etf.repositories.FitnessProgramParticipationRepository;
import org.unibl.etf.repositories.FitnessProgramRepository;
import org.unibl.etf.services.FitnessProgramService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class FitnessProgramServiceImpl implements FitnessProgramService {

    private final ModelMapper mapper;
    private final FitnessProgramRepository fitnessProgramRepository;

    private final FitnessProgramAttributeRepository fitnessProgramAttributeRepository;

    private final FitnessProgramParticipationRepository fitnessProgramParticipationRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public FitnessProgramServiceImpl(ModelMapper mapper, FitnessProgramRepository fitnessProgramRepository, FitnessProgramAttributeRepository fitnessProgramAttributeRepository, FitnessProgramParticipationRepository fitnessProgramParticipationRepository) {
        this.mapper = mapper;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.fitnessProgramAttributeRepository = fitnessProgramAttributeRepository;
        this.fitnessProgramParticipationRepository = fitnessProgramParticipationRepository;
    }

    @Override
    public FitnessProgramDTO insert(Long id, FitnessProgramRequestDTO requestDTO, Authentication auth) {
        var jwtUser=(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        var entity=mapper.map(requestDTO, FitnessProgramEntity.class);
        entity.setId(null);
        entity.setDifficulty(Difficulty.getByStatus(requestDTO.getDifficulty()));
        entity.setLocation(Location.getByStatus(requestDTO.getLocation()));
        ImageEntity image=new ImageEntity(); image.setId(requestDTO.getImageId());
        entity.setImage(image);
        CategoryEntity category=new CategoryEntity(); category.setId(requestDTO.getCategoryId());
        ClientEntity client=new ClientEntity(); client.setId(id);
        entity.setClient(client);
        entity.setCategory(category);
        entity.setStatus(true);
        entity.setCreatedAt(new Date());
        entity=this.fitnessProgramRepository.saveAndFlush(entity);
        this.entityManager.refresh(entity);
        for(var attr:requestDTO.getAttributes()){
            var newAttr=new FitnessProgramAttributeEntity();
            var tmp=new CategoryAttributeEntity(); tmp.setId(attr.getId());
            newAttr.setAttribute(tmp);
            newAttr.setFitnessProgram(entity);
            newAttr.setValue(attr.getValue());
            this.fitnessProgramAttributeRepository.saveAndFlush(newAttr);
        }
        this.entityManager.refresh(entity);
        return mapper.map(entity, FitnessProgramDTO.class);
    }

    @Override
    public List<FitnessProgramDTO> findAll() {
        return this.fitnessProgramRepository.findAllByStatus(true).stream().map(el->mapper.map(el, FitnessProgramDTO.class)).toList();
    }

    @Override
    public FitnessProgramDTO findById(Long id) {
        var entity=this.fitnessProgramRepository.findById(id).orElseThrow(NotFoundException::new);
        return mapper.map(entity, FitnessProgramDTO.class);
    }

    @Override
    public void participateClient(Long cliendId, Long fpId, Authentication auth) {
        var jwtUser=(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(cliendId))
            throw new UnauthorizedException();
        var fitnessProgram=this.fitnessProgramRepository.findById(fpId).orElseThrow(NotFoundException::new);
        ClientEntity client=new ClientEntity(); client.setId(cliendId);
        var fpP=new FitnessProgramParticipationEntity();
        fpP.setClient(client); fpP.setFitnessProgram(fitnessProgram);
        fpP.setStartDate(new Date());
        this.fitnessProgramParticipationRepository.saveAndFlush(fpP);
    }

    //ako mi vrati false moze da se prikaze dugme, ako je true ne moze
    @Override
    public boolean isClientParticipatingInFp(Long cliendId, Long fpId, Authentication auth) {
        var jwtUser=(JwtUserDTO)auth.getPrincipal();
        if(!jwtUser.getId().equals(cliendId))
            throw new UnauthorizedException();
        var entity= this.fitnessProgramParticipationRepository.findByClientIdAndFitnessProgramId(cliendId,fpId);
        if(entity.isEmpty())
            return false;
        var fp=this.fitnessProgramRepository.findById(fpId).orElseThrow(NotFoundException::new);
        var tmp=entity.get();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmp.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, fp.getDuration());
        Date programEndDate = calendar.getTime();
        System.out.println(programEndDate);
        Date currentDate = new Date();
        return programEndDate.after(currentDate);


    }
}
