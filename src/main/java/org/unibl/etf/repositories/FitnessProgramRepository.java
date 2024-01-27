package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity,Long>, JpaSpecificationExecutor<FitnessProgramEntity> {
    List<FitnessProgramEntity> findAllByStatus(boolean status);
    List<FitnessProgramEntity> findAllByClientIdAndStatus(Long id,boolean status);
    List<FitnessProgramEntity> findByCategoryIdAndCreatedAtBetween(Long categoryId, Date start,Date end);
    Optional<FitnessProgramEntity> findByIdAndStatus(Long id,boolean status);
}
