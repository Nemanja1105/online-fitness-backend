package org.unibl.etf.services;

import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.FitnessProgramCommentDTO;
import org.unibl.etf.models.dto.FitnessProgramCommentRequestDTO;
import org.unibl.etf.models.dto.FitnessProgramDTO;
import org.unibl.etf.models.dto.FitnessProgramRequestDTO;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.List;

public interface FitnessProgramService {
    FitnessProgramDTO insert(Long id, FitnessProgramRequestDTO requestDTO, Authentication auth);
    List<FitnessProgramDTO> findAll();
    FitnessProgramDTO findById(Long id);
    void participateClient(Long cliendId,Long fpId,Authentication auth);
    boolean isClientParticipatingInFp(Long cliendId,Long fpId,Authentication auth);
    FitnessProgramCommentDTO commentFitnessProgram(FitnessProgramCommentRequestDTO requestDTO);
    List<FitnessProgramCommentDTO> findAllCommentsForFp(Long id);
}
