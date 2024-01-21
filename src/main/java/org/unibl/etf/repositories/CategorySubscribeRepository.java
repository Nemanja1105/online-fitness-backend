package org.unibl.etf.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.unibl.etf.models.dto.CategorySubscribeDTO;
import org.unibl.etf.models.entities.CategorySubscribeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategorySubscribeRepository extends JpaRepository<CategorySubscribeEntity,Long> {
    @Query("SELECT new org.unibl.etf.models.dto.CategorySubscribeDTO(c.id, c.name, CASE WHEN cs.client.id IS NOT NULL THEN true ELSE false END) " +
            "FROM category c " +
            "LEFT JOIN category_subscribe cs ON c.id = cs.category.id AND cs.client.id = :clientId "+
    "WHERE c.status=true")
    List<CategorySubscribeDTO> getAllCategoriesWithSubscriptionStatus(@Param("clientId") Long clientId);

    Optional<CategorySubscribeEntity> findByCategoryIdAndClientId(Long categoryId,Long clientId);
}
