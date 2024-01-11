package org.unibl.etf.services;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendVerificationEmail(String token,String to);
}
