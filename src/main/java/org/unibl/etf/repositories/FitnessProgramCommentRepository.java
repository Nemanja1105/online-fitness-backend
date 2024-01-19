package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.FitnessProgramCommentEntity;

import java.util.List;

@Repository
public interface FitnessProgramCommentRepository extends JpaRepository<FitnessProgramCommentEntity,Long> {
    List<FitnessProgramCommentEntity> findAllByFitnessProgramIdOrderByCreatedAtDesc(Long id);
}
