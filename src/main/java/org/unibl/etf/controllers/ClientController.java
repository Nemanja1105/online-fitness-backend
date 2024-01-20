package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.*;
import org.unibl.etf.services.ClientService;
import org.unibl.etf.services.FitnessProgramService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;
    private final FitnessProgramService fitnessProgramService;

    public ClientController(ClientService clientService, FitnessProgramService fitnessProgramService) {
        this.clientService = clientService;
        this.fitnessProgramService = fitnessProgramService;
    }

    @PostMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody UpdateClientDTO request, Authentication auth){
        return this.clientService.updateClient(id,request,auth);
    }

    @PostMapping("/{id}/fitness-program")
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




}
