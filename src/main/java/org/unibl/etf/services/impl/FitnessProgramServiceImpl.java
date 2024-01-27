package org.unibl.etf.services.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.models.entities.*;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.Location;
import org.unibl.etf.models.specifications.FitnessProgramSpecification;
import org.unibl.etf.repositories.FitnessProgramAttributeRepository;
import org.unibl.etf.repositories.FitnessProgramCommentRepository;
import org.unibl.etf.repositories.FitnessProgramParticipationRepository;
import org.unibl.etf.repositories.FitnessProgramRepository;
import org.unibl.etf.services.FitnessProgramService;
import org.unibl.etf.services.LogService;

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

    private final FitnessProgramCommentRepository fitnessProgramCommentRepository;

    private final LogService logService;
    private final HttpServletRequest request;
    @PersistenceContext
    private EntityManager entityManager;

    public FitnessProgramServiceImpl(ModelMapper mapper, FitnessProgramRepository fitnessProgramRepository, FitnessProgramAttributeRepository fitnessProgramAttributeRepository, FitnessProgramParticipationRepository fitnessProgramParticipationRepository, FitnessProgramCommentRepository fitnessProgramCommentRepository, LogService logService, HttpServletRequest request) {
        this.mapper = mapper;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.fitnessProgramAttributeRepository = fitnessProgramAttributeRepository;
        this.fitnessProgramParticipationRepository = fitnessProgramParticipationRepository;
        this.fitnessProgramCommentRepository = fitnessProgramCommentRepository;
        this.logService = logService;
        this.request = request;
    }

    @Override
    public FitnessProgramDTO insert(Long id, FitnessProgramRequestDTO requestDTO, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(id)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var entity = mapper.map(requestDTO, FitnessProgramEntity.class);
        entity.setId(null);
        entity.setDifficulty(Difficulty.getByStatus(requestDTO.getDifficulty()));
        entity.setLocation(Location.getByStatus(requestDTO.getLocation()));
        ImageEntity image = new ImageEntity();
        image.setId(requestDTO.getImageId());
        entity.setImage(image);
        CategoryEntity category = new CategoryEntity();
        category.setId(requestDTO.getCategoryId());
        ClientEntity client = new ClientEntity();
        client.setId(id);
        entity.setClient(client);
        entity.setCategory(category);
        entity.setStatus(true);
        entity.setLinkAddress(requestDTO.getLocationLink());
        entity.setCreatedAt(new Date());
        entity = this.fitnessProgramRepository.saveAndFlush(entity);
        this.entityManager.refresh(entity);
        for (var attr : requestDTO.getAttributes()) {
            var newAttr = new FitnessProgramAttributeEntity();
            var tmp = new CategoryAttributeEntity();
            tmp.setId(attr.getId());
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
        var result= this.fitnessProgramRepository.findAllByStatus(true).stream().map(el -> mapper.map(el, FitnessProgramDTO.class)).toList();
        return result;
    }

    @Override
    public FitnessProgramDTO findById(Long id) {
        var entity = this.fitnessProgramRepository.findByIdAndStatus(id,true).orElseThrow(NotFoundException::new);
        return mapper.map(entity, FitnessProgramDTO.class);
    }

    @Override
    public void participateClient(Long cliendId, Long fpId, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(cliendId)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var fitnessProgram = this.fitnessProgramRepository.findById(fpId).orElseThrow(NotFoundException::new);
        var optionalPart=this.fitnessProgramParticipationRepository.findByClientIdAndFitnessProgramId(cliendId,fpId);
        if(optionalPart.isPresent()){
            var optionalTmp=optionalPart.get();
            if(!isAfter(optionalTmp.getStartDate(),fitnessProgram.getDuration()))
                optionalTmp.setStartDate(new Date());
            this.logService.info("Client "+jwtUser.getUsername()+" successfully subscribed to the fitness program.");
            return;
        }
        ClientEntity client = new ClientEntity();
        client.setId(cliendId);
        var fpP = new FitnessProgramParticipationEntity();
        fpP.setClient(client);
        fpP.setFitnessProgram(fitnessProgram);
        fpP.setStartDate(new Date());
        this.fitnessProgramParticipationRepository.saveAndFlush(fpP);
        this.logService.info("Client "+jwtUser.getUsername()+" successfully subscribed to the fitness program.");
    }

    //ako mi vrati false moze da se prikaze dugme, ako je true ne moze
    @Override
    public boolean isClientParticipatingInFp(Long cliendId, Long fpId, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(cliendId)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var entity = this.fitnessProgramParticipationRepository.findByClientIdAndFitnessProgramId(cliendId, fpId);
        if (entity.isEmpty())
            return false;
        var fp = this.fitnessProgramRepository.findById(fpId).orElseThrow(NotFoundException::new);
        var tmp = entity.get();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(tmp.getStartDate());
        calendar.add(Calendar.DAY_OF_MONTH, fp.getDuration());
        Date programEndDate = calendar.getTime();
        System.out.println(programEndDate);
        Date currentDate = new Date();
        this.logService.info("Client "+jwtUser.getUsername()+" has successfully completed the subscription verification");
        return programEndDate.after(currentDate);
    }

    private boolean isAfter(Date startDate,Integer duration){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_MONTH, duration);
        Date programEndDate = calendar.getTime();
        Date currentDate = new Date();
        return programEndDate.after(currentDate);
    }

    @Override
    public FitnessProgramCommentDTO commentFitnessProgram(FitnessProgramCommentRequestDTO requestDTO) {
        if (!this.fitnessProgramRepository.existsById(requestDTO.getFitnessProgramId()))
            throw new NotFoundException();
        FitnessProgramCommentEntity entity = new FitnessProgramCommentEntity();
        entity.setComment(requestDTO.getComment());
        entity.setCreatedAt(new Date());
        ClientEntity client = new ClientEntity();
        client.setId(requestDTO.getSenderId());
        entity.setSender(client);
        FitnessProgramEntity fp = new FitnessProgramEntity();
        fp.setId(requestDTO.getFitnessProgramId());
        entity.setFitnessProgram(fp);
        entity = this.fitnessProgramCommentRepository.saveAndFlush(entity);
        entityManager.refresh(entity);
        this.logService.info("Client "+request.getRemoteAddr()+" successfully commented on the fitness program");
        return mapper.map(entity, FitnessProgramCommentDTO.class);

    }

    @Override
    public List<FitnessProgramCommentDTO> findAllCommentsForFp(Long id) {
        return this.fitnessProgramCommentRepository.findAllByFitnessProgramIdOrderByCreatedAtDesc(id).stream()
                .map(el->mapper.map(el,FitnessProgramCommentDTO.class)).toList();
    }

    @Override
    public Page<FitnessProgramDTO> findAllByFilters(List<FilterDTO> filters, Pageable pageable) {
        filters.add(new FilterDTO("status",true));
        var spec= FitnessProgramSpecification.filters(filters);
        return this.fitnessProgramRepository.findAll(spec,pageable).map(el->mapper.map(el,FitnessProgramDTO.class));
    }

    @Override
    public List<FitnessProgramDTO> findAllFpForClient(Long id, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(id)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        return this.fitnessProgramRepository.findAllByClientIdAndStatus(id,true).stream()
                .map(el->mapper.map(el,FitnessProgramDTO.class)).toList();
    }

    @Override
    public void deleteFp(Long clientId, Long fpId, Authentication auth) {
        var fp=this.fitnessProgramRepository.findById(fpId).orElseThrow(NotFoundException::new);
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(fp.getClient().getId())) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        fp.setStatus(false);
        logService.info("Client "+jwtUser.getUsername()+" has successfully deleted the fitness program.");
    }

    @Override
    public List<FitnessProgramDTO> findAllActiveFpForClient(Long id, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(id)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var participation=this.fitnessProgramParticipationRepository.findAllByClientId(id);
       return participation.stream().filter(el->this.isAfter(el.getStartDate(),el.getFitnessProgram().getDuration())).map(el->mapper.map(el.getFitnessProgram(),FitnessProgramDTO.class)).toList();
    }

    @Override
    public List<FitnessProgramDTO> findAllFinishedFpForClient(Long id, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(id)) {
            this.logService.warning("Attempted action on someone else's account. Client:"+jwtUser.getUsername()+".");
            throw new UnauthorizedException();
        }
        var participation=this.fitnessProgramParticipationRepository.findAllByClientId(id);
        return participation.stream().filter(el->!this.isAfter(el.getStartDate(),el.getFitnessProgram().getDuration())).map(el->mapper.map(el.getFitnessProgram(),FitnessProgramDTO.class)).toList();
    }


}
