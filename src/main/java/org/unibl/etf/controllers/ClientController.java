package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.services.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final FitnessProgramService fitnessProgramService;

    private  final MessageService messageService;
    private final ActivityService activityService;
    private final BodyWeightService bodyWeightService;
    private final PdfService pdfService;

    public ClientController(ClientService clientService, FitnessProgramService fitnessProgramService, MessageService messageService, ActivityService activityService, BodyWeightService bodyWeightService, PdfService pdfService) {
        this.clientService = clientService;
        this.fitnessProgramService = fitnessProgramService;
        this.messageService = messageService;
        this.activityService = activityService;
        this.bodyWeightService = bodyWeightService;
        this.pdfService = pdfService;
    }

    @GetMapping
    public List<ClientInfoDTO> findAll(){
        return this.clientService.findAll();
    }

    @PostMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody UpdateClientDTO request, Authentication auth){
        return this.clientService.updateClient(id,request,auth);
    }

    @PostMapping("/{id}/fitness-program")
    @ResponseStatus(HttpStatus.CREATED)
    public FitnessProgramDTO insert(@PathVariable Long id,@Valid @RequestBody FitnessProgramRequestDTO requestDTO,Authentication authentication){
        return this.fitnessProgramService.insert(id,requestDTO,authentication);
    }

    @GetMapping("/{id}/fitness-program/{fpId}/subscribe")
    public void participateClientToProgram(@PathVariable Long id,@PathVariable Long fpId,Authentication auth){
        this.fitnessProgramService.participateClient(id,fpId,auth);
    }

    @GetMapping("/{id}/fitness-program/{fpId}/participating")
    public boolean isClientParticipatingInFp(@PathVariable Long id,@PathVariable Long fpId,Authentication auth){
       return this.fitnessProgramService.isClientParticipatingInFp(id,fpId,auth);
    }

    @GetMapping("/{id}/fitness-programs")
    public List<FitnessProgramDTO> findAllFpForClient(@PathVariable Long id, Authentication auth){
        return this.fitnessProgramService.findAllFpForClient(id,auth);
    }

    @DeleteMapping("/{id}/fitness-programs/{fpId}")
    public void deleteFp(@PathVariable Long id,@PathVariable Long fpId,Authentication auth){
        this.fitnessProgramService.deleteFp(id,fpId,auth);
    }

    @GetMapping("/{id}/fitness-programs/active")
    public List<FitnessProgramDTO> findAllActiveFpForClient(@PathVariable Long id,Authentication auth){
        return this.fitnessProgramService.findAllActiveFpForClient(id,auth);
    }

    @GetMapping("/{id}/fitness-programs/finished")
    public List<FitnessProgramDTO> findAllFinishedFpForClient(@PathVariable Long id,Authentication auth){
        return this.fitnessProgramService.findAllFinishedFpForClient(id,auth);
    }

    @GetMapping("/{id}/messages")
    public Page<MessageDTO> findAllClientMessages(@PathVariable Long id, Authentication auth, Pageable page){
        return this.messageService.findAllMessageForClient(id,auth,page);
    }

    @GetMapping("/{id}/activities")
    public List<ActivityDTO> findAllActivitiesForClient(@PathVariable Long id,Authentication authentication){
        return this.activityService.findAllActivitiesForClient(id,authentication);
    }

    @PostMapping("/{id}/activities")
    @ResponseStatus(HttpStatus.CREATED)
    public ActivityDTO insertActivityForClient(@PathVariable Long id,@Valid @RequestBody ActivityRequestDTO requestDTO,Authentication authentication){
        return this.activityService.insertActivityForClient(id,requestDTO,authentication);
    }

    @DeleteMapping("/{clientId}/activities/{activityId}")
    public void deleteActivityForClient(@PathVariable Long clientId,@PathVariable Long activityId,Authentication authentication){
        this.activityService.deleteActivity(clientId,activityId,authentication);
    }

    @PostMapping("/{id}/bodyweights")
    @ResponseStatus(HttpStatus.CREATED)
    public BodyWeightDTO insertBodyWeightForClient(@PathVariable Long id,@Valid @RequestBody BodyWeightRequestDTO requestDTO,Authentication authentication){
        return this.bodyWeightService.insertBodyWeightForClient(id,requestDTO,authentication);
    }

    @PostMapping("/{id}/bodyweights/statistic")
    public BodyWeightStatisticDTO findStatisticForClient(@PathVariable Long id,@Valid @RequestBody BodyWeightFilterDTO request,Authentication authentication){
        return this.bodyWeightService.findStatisticForClient(id,request,authentication);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<?> downloadPdf(@PathVariable Long id) throws IOException {
        PdfDTO pdfDTO = pdfService.generatePdfForClient(id);
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("application/pdf"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + pdfDTO.getFileName()+".pdf" + "\"")
                .body(pdfDTO.getData());
    }




}
