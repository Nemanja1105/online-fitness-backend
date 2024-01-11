package org.unibl.etf.services.impl;

import org.springframework.stereotype.Service;
import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.entities.ValidationTokenEntity;
import org.unibl.etf.repositories.ValidationTokenRepository;
import org.unibl.etf.services.ValidationTokenService;

import java.util.UUID;

@Service
public class ValidationTokenServiceImpl implements ValidationTokenService {
    private final ValidationTokenRepository validationTokenRepository;

    public ValidationTokenServiceImpl(ValidationTokenRepository validationTokenRepository) {
        this.validationTokenRepository = validationTokenRepository;
    }


    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public ValidationTokenEntity generateTokenForUser(ClientEntity user) {
        var token=this.generateToken();
        var validationToken= ValidationTokenEntity.builder().token(token).client(user).build();
        this.validationTokenRepository.saveAndFlush(validationToken);
        return validationToken;
    }
}
