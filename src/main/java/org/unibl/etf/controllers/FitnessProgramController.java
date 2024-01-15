package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.dto.FitnessProgramDTO;
import org.unibl.etf.services.FitnessProgramService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/fitness-programs")
public class FitnessProgramController {

    private final FitnessProgramService fitnessProgramService;

    public FitnessProgramController(FitnessProgramService fitnessProgramService) {
        this.fitnessProgramService = fitnessProgramService;
    }

    @GetMapping
    public List<FitnessProgramDTO> findAll(){
        return this.fitnessProgramService.findAll();
    }
}
