package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramEntity;

import java.util.List;

@Repository
public interface FitnessProgramRepository extends JpaRepository<FitnessProgramEntity,Long> {
    List<FitnessProgramEntity> findAllByStatus(boolean status);
}
