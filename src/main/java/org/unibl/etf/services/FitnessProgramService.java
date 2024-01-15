package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.FitnessProgramDTO;
import org.unibl.etf.models.dto.FitnessProgramRequestDTO;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.List;

public interface FitnessProgramService {
    FitnessProgramDTO insert(Long id, FitnessProgramRequestDTO requestDTO, Authentication auth);
    List<FitnessProgramDTO> findAll();
}
