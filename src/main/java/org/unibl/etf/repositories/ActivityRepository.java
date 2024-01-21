package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.entities.ActivityEntity;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity,Long> {
    List<ActivityEntity> findAllByClientIdAndStatus(Long clientId,boolean status);
}
