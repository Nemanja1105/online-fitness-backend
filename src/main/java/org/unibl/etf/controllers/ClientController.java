package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.FitnessProgramDTO;
import org.unibl.etf.models.dto.FitnessProgramRequestDTO;
import org.unibl.etf.models.dto.UpdateClientDTO;
import org.unibl.etf.services.ClientService;
import org.unibl.etf.services.FitnessProgramService;

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


}
