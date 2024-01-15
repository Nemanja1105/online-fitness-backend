package org.unibl.etf.services.impl;

import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.dto.CategoryDTO;
import org.unibl.etf.models.entities.CategoryAttributeEntity;
import org.unibl.etf.repositories.CategoryRepository;
import org.unibl.etf.services.CategoryService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return this.categoryRepository.findAllByStatus(true).stream().map((el)->{
            var tmp= el.getAttributes().stream().filter(CategoryAttributeEntity::isStatus).toList();
            el.setAttributes(tmp);
            return mapper.map(el,CategoryDTO.class);}).toList();
    }
}
