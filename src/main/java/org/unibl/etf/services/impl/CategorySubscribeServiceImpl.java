package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.unibl.etf.exceptions.NotFoundException;
import org.unibl.etf.exceptions.UnauthorizedException;
import org.unibl.etf.models.dto.CategorySubscribeDTO;
import org.unibl.etf.models.dto.JwtUserDTO;
import org.unibl.etf.models.entities.CategoryEntity;
import org.unibl.etf.models.entities.CategorySubscribeEntity;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.repositories.CategoryRepository;
import org.unibl.etf.repositories.CategorySubscribeRepository;
import org.unibl.etf.repositories.ClientRepository;
import org.unibl.etf.repositories.FitnessProgramRepository;
import org.unibl.etf.services.CategorySubscribeService;
import org.unibl.etf.services.EmailService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;



@Service
@Transactional
public class CategorySubscribeServiceImpl implements CategorySubscribeService {

    private final CategorySubscribeRepository categorySubscribeRepository;
    private final CategoryRepository categoryRepository;
    private final ClientRepository clientRepository;
    private final FitnessProgramRepository fitnessProgramRepository;

    private final EmailService emailService;

    @Scheduled(cron="0 0 21 * * *")//9 navece
    public void sendEmailNotification(){
        var subscribers=this.categorySubscribeRepository.findAll();
        LocalDateTime current = LocalDateTime.now();
        var currentDate= Date.from(current.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime yesterDay=current.minusDays(1);
        var yesterdayDate=Date.from(yesterDay.atZone(ZoneId.systemDefault()).toInstant());
        for(var sub:subscribers){
            var client=sub.getClient();
            var category=sub.getCategory();
            var fitnessPrograms=this.fitnessProgramRepository.findByCategoryIdAndCreatedAtBetween(category.getId(), yesterdayDate,currentDate);
            if(!fitnessPrograms.isEmpty()) {
                StringBuilder builder = new StringBuilder("Fitness programs created the previous day for the category \""+category.getName()+"\":\n");
                for (var fp : fitnessPrograms)
                    builder.append("\t" + fp.getName() + "\n");
                this.emailService.sendInfoMail(builder.toString(),client.getEmail());
            }


        }
    }

    public CategorySubscribeServiceImpl(CategorySubscribeRepository categorySubscribeRepository, CategoryRepository categoryRepository, ClientRepository clientRepository, FitnessProgramRepository fitnessProgramRepository, EmailService emailService) {
        this.categorySubscribeRepository = categorySubscribeRepository;
        this.categoryRepository = categoryRepository;
        this.clientRepository = clientRepository;
        this.fitnessProgramRepository = fitnessProgramRepository;
        this.emailService = emailService;
    }

    @Override
    public List<CategorySubscribeDTO> findAllForClient(Long id, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(id))
            throw new UnauthorizedException();
        return this.categorySubscribeRepository.getAllCategoriesWithSubscriptionStatus(id);
    }

    @Override
    public void changeSubscribeForClient(Long categoryId, Long clientId, Authentication auth) {
        var jwtUser = (JwtUserDTO) auth.getPrincipal();
        if (!jwtUser.getId().equals(clientId))
            throw new UnauthorizedException();
        if(!this.categoryRepository.existsById(categoryId) || !this.clientRepository.existsById(clientId))
            throw new NotFoundException();

        var categorySubsOptional=this.categorySubscribeRepository.findByCategoryIdAndClientId(categoryId,clientId);
        if(categorySubsOptional.isPresent()){
            var tmp=categorySubsOptional.get();
            this.categorySubscribeRepository.delete(tmp);//brisemo pretplatu ako postoji
        }
        else{
            ClientEntity client=new ClientEntity(); client.setId(clientId);
            CategoryEntity category=new CategoryEntity(); category.setId(categoryId);
            CategorySubscribeEntity categorySubscribeEntity=new CategorySubscribeEntity();
            categorySubscribeEntity.setId(null);
            categorySubscribeEntity.setCategory(category);
            categorySubscribeEntity.setClient(client);
            this.categorySubscribeRepository.saveAndFlush(categorySubscribeEntity);
        }
    }
}
