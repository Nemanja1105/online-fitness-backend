package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.dto.EmailDTO;
import org.unibl.etf.services.EmailService;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping
    public void sendEmail(@RequestBody EmailDTO emailDTO){
        this.emailService.sendAdvisorMail(emailDTO);
    }
}
