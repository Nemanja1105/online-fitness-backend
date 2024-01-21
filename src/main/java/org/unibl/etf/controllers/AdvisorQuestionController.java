package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.dto.AdvisorQuestionRequestDTO;
import org.unibl.etf.services.AdvisorQuestionService;

@RestController
@RequestMapping("/api/v1/advisor-questions")
public class AdvisorQuestionController {

    private final AdvisorQuestionService advisorQuestionService;

    public AdvisorQuestionController(AdvisorQuestionService advisorQuestionService) {
        this.advisorQuestionService = advisorQuestionService;
    }

    @PostMapping
    public void insert(@Valid @RequestBody AdvisorQuestionRequestDTO requestDTO, Authentication authentication){
        this.advisorQuestionService.sendQuestion(requestDTO,authentication);
    }
}
