package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.List;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity,Long>, JpaSpecificationExecutor<FitnessProgramEntity> {
    List<FitnessProgramEntity> findAllByStatus(boolean status);
    List<FitnessProgramEntity> findAllByClientIdAndStatus(Long id,boolean status);
}
