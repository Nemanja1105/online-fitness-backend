package org.unibl.etf.services;

import org.springframework.scheduling.annotation.Async;
import org.unibl.etf.models.dto.EmailDTO;

public interface EmailService {
    @Async
    void sendVerificationEmail(String token,String to);
    @Async
     void sendInfoMail(String mail,String to);

    @Async
    void sendAdvisorMail(EmailDTO emailDTO);
}
