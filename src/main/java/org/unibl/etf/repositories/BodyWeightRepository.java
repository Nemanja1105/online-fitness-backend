package org.unibl.etf.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.BodyWeightEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface BodyWeightRepository extends JpaRepository<BodyWeightEntity,Long> {
    List<BodyWeightEntity> findAllByClientIdAndCreatedAtBetweenOrderByCreatedAtAsc(Long clientId,Date startDate, Date endDate);
    List<BodyWeightEntity> findAllByClientIdAndCreatedAtAfterOrderByCreatedAtAsc(Long clientId,Date startDate);
    List<BodyWeightEntity> findAllByClientIdAndCreatedAtBeforeOrderByCreatedAtAsc(Long clientId,Date endDate);
    List<BodyWeightEntity> findAllByClientIdOrderByCreatedAtAsc(Long clientId);

}
