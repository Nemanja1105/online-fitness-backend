package org.unibl.etf.services.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.unibl.etf.services.EmailService;

import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${account.verification.url}")
    private String accountVerificationUrl;

    @Async
    public void sendVerificationEmail(String token,String to){
        try {
            MimeMessage message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setSubject("Account verification, Pocket Wedding");
            ClassPathResource htmlPath = new ClassPathResource("AccountVerification.html");
            var html= Files.readString(Path.of(htmlPath.getFile().getAbsolutePath()));
            html=html.replace("validation.url",accountVerificationUrl+token);
            helper.setText(html,true);
            helper.setFrom(fromMail);
            helper.setTo(to);
            System.out.println(fromMail);
            this.mailSender.send(message);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendInfoMail(String mail,String to){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject("Daily Fitness programs");
            message.setText(mail);
            this.mailSender.send(message);
        }
        catch(Exception e){
            throw new RuntimeException();
        }
    }
}
