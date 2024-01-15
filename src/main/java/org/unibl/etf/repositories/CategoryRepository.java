package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.models.entities.CategoryEntity;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {
    List<CategoryEntity> findAllByStatus(boolean status);
}
