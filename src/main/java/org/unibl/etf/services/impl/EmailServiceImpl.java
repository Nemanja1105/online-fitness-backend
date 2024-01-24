package org.unibl.etf.services.impl;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.dto.EmailDTO;
import org.unibl.etf.services.EmailService;
import org.unibl.etf.services.ImageService;
import org.unibl.etf.services.LogService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Transactional
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final ImageService imageService;

    public EmailServiceImpl(JavaMailSender mailSender, ImageService imageService, HttpServletRequest request, LogService logService) {
        this.mailSender = mailSender;
        this.imageService = imageService;
        this.request = request;
        this.logService = logService;
    }

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${account.verification.url}")
    private String accountVerificationUrl;

    private final HttpServletRequest request;
    private final LogService logService;

    @Async
    public void sendVerificationEmail(String token, String to) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setSubject("Account verification, Pocket Wedding");
            ClassPathResource htmlPath = new ClassPathResource("AccountVerification.html");
            var html = Files.readString(Path.of(htmlPath.getFile().getAbsolutePath()));
            html = html.replace("validation.url", accountVerificationUrl + token);
            helper.setText(html, true);
            helper.setFrom(fromMail);
            helper.setTo(to);
            System.out.println(fromMail);
            this.mailSender.send(message);
            this.logService.info("Successfully sent a verification email to the client "+request.getRemoteAddr());
        } catch (Exception e) {
            this.logService.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Async
    public void sendInfoMail(String mail, String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromMail);
            message.setTo(to);
            message.setSubject("Daily Fitness programs");
            message.setText(mail);
            this.mailSender.send(message);
        } catch (Exception e) {
            this.logService.error(e.getMessage());
            throw new RuntimeException();
        }
    }

    @Override
    public void sendAdvisorMail(EmailDTO emailDTO) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromMail);
            helper.setTo(emailDTO.getTo());
            if (emailDTO.getSubject() != null) {
                helper.setSubject(emailDTO.getSubject());
            }
            if (emailDTO.getAttachmentId() != null) {
                var path = this.imageService.getPathById(emailDTO.getAttachmentId());
                FileSystemResource file = new FileSystemResource(new File(path[0]));
                helper.addAttachment(path[1], file);
            }

            helper.setText(emailDTO.getMessage());
            this.mailSender.send(message);
            if (emailDTO.getAttachmentId() != null)
                this.imageService.deleteImageById(emailDTO.getAttachmentId());
            this.logService.info("Advisor "+request.getRemoteAddr()+" successfully sent an email to the client");
        } catch (Exception e) {
            this.logService.error(e.getMessage());
            throw new RuntimeException();
        }
    }
}
