package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.AdvisorQuestionRequestDTO;

public interface AdvisorQuestionService {
    void sendQuestion(AdvisorQuestionRequestDTO requestDTO, Authentication auth);
}
