package org.unibl.etf.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.unibl.etf.models.dto.*;
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
    Page<FitnessProgramDTO> findAllByFilters(List<FilterDTO> filters, Pageable pageable);
    List<FitnessProgramDTO> findAllFpForClient(Long id,Authentication auth);
    void deleteFp(Long clientId,Long fpId,Authentication auth);
    List<FitnessProgramDTO> findAllActiveFpForClient(Long id,Authentication auth);
    List<FitnessProgramDTO> findAllFinishedFpForClient(Long id,Authentication auth);
}
