package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramParticipationEntity;

import java.util.Optional;

@Repository
public interface FitnessProgramParticipationRepository  extends JpaRepository<FitnessProgramParticipationEntity,Long> {
    Optional<FitnessProgramParticipationEntity> findByClientIdAndFitnessProgramId(Long clientId,Long fitnessProgramId);
}
