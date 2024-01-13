package org.unibl.etf.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.ClientDTO;
import org.unibl.etf.models.dto.UpdateClientDTO;
import org.unibl.etf.services.ClientService;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/{id}")
    public ClientDTO updateClient(@PathVariable Long id, @RequestBody UpdateClientDTO request, Authentication auth){
        return this.clientService.updateClient(id,request,auth);
    }
}
