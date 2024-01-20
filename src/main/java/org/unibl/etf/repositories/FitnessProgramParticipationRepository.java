package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramEntity;
import org.unibl.etf.models.entities.FitnessProgramParticipationEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessProgramParticipationRepository  extends JpaRepository<FitnessProgramParticipationEntity,Long> {
    Optional<FitnessProgramParticipationEntity> findByClientIdAndFitnessProgramId(Long clientId,Long fitnessProgramId);
    List<FitnessProgramParticipationEntity> findAllByClientId(Long clientId);
//fitness_program_participation

}
