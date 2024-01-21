package org.unibl.etf.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.MessageDTO;
import org.unibl.etf.models.dto.MessageRequestDTO;
import org.unibl.etf.services.MessageService;

@RestController
@RequestMapping("/api/v1/messages")
public class MessageController {
    private  final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public MessageDTO insertMessage(@RequestBody MessageRequestDTO requestDTO, Authentication auth){
        return this.messageService.sendMessage(requestDTO,auth);
    }

    @GetMapping("/{id}/seen")
    public void markAsRead(@PathVariable Long id,Authentication auth){
        this.messageService.markAsRead(id,auth);
    }
}
