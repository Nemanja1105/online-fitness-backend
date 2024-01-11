package org.unibl.etf.services;

import org.unibl.etf.models.entities.ClientEntity;
import org.unibl.etf.models.entities.ValidationTokenEntity;

public interface ValidationTokenService {
    ValidationTokenEntity generateTokenForUser(ClientEntity user);
}
