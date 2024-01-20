package org.unibl.etf.controllers;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.FilterDTO;
import org.unibl.etf.models.dto.FitnessProgramCommentDTO;
import org.unibl.etf.models.dto.FitnessProgramCommentRequestDTO;
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

    @GetMapping("/{id}")
    public FitnessProgramDTO findById(@PathVariable Long id){
        return this.fitnessProgramService.findById(id);
    }

    @PostMapping("/{id}/comments")
    public FitnessProgramCommentDTO commentFitnessProgram(@Valid @RequestBody FitnessProgramCommentRequestDTO requestDTO){
        return this.fitnessProgramService.commentFitnessProgram(requestDTO);
    }

    @GetMapping("/{id}/comments")
    public  List<FitnessProgramCommentDTO> findAllCommentsForFp(@PathVariable Long id){
        return this.fitnessProgramService.findAllCommentsForFp(id);
    }

    @PostMapping("/filters")
    public Page<FitnessProgramDTO> findAllByFilters(@RequestBody List<FilterDTO> filters, Pageable page){
        return this.fitnessProgramService.findAllByFilters(filters,page);
    }
}
