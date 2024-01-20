package org.unibl.etf.models.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.unibl.etf.models.dto.FilterDTO;
import org.unibl.etf.models.entities.FitnessProgramEntity;
import org.unibl.etf.models.enums.Difficulty;
import org.unibl.etf.models.enums.Location;

import java.util.ArrayList;
import java.util.List;

public class FitnessProgramSpecification {
    public static Specification<FitnessProgramEntity> filters(List<FilterDTO> filterDTOList) {
        return new Specification<FitnessProgramEntity>() {
            @Override
            public Predicate toPredicate(Root<FitnessProgramEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                filterDTOList.forEach(filter -> {
                    Predicate predicate;
                    if(filter.getColumnName().equals("search"))
                        predicate=criteriaBuilder.like(root.get("name"),filter.getColumnValue()+"%");
                    else if(filter.getColumnName().equals("category"))
                        predicate=criteriaBuilder.equal(root.get(filter.getColumnName()).get("id"),filter.getColumnValue());
                    else if(filter.getColumnName().equals("location"))
                    {
                        predicate=criteriaBuilder.equal(root.get("location"), Location.getByStatus((String)filter.getColumnValue()));
                    }
                    else if(filter.getColumnName().equals("difficulty")){
                        predicate=criteriaBuilder.equal(root.get("difficulty"), Difficulty.getByStatus((String)filter.getColumnValue()));
                    }
                    else
                        predicate = criteriaBuilder.equal(root.get(filter.getColumnName()), filter.getColumnValue());
                    predicates.add(predicate);
                });
                return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
            }

        };
    }
}
