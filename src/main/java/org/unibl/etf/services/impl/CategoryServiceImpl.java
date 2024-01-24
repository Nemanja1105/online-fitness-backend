package org.unibl.etf.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.unibl.etf.models.dto.CategoryDTO;
import org.unibl.etf.models.entities.CategoryAttributeEntity;
import org.unibl.etf.repositories.CategoryRepository;
import org.unibl.etf.services.CategoryService;
import org.unibl.etf.services.LogService;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final LogService logService;

    private final HttpServletRequest request;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper mapper, LogService logService, HttpServletRequest request) {
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
        this.logService = logService;
        this.request = request;
    }

    @Override
    public List<CategoryDTO> findAll() {
        this.logService.info("Client "+request.getRemoteAddr()+" tries to get all categories");
        return this.categoryRepository.findAllByStatus(true).stream().map((el)->{
            var tmp= el.getAttributes().stream().filter(CategoryAttributeEntity::isStatus).toList();
            el.setAttributes(tmp);
            return mapper.map(el,CategoryDTO.class);}).toList();
    }
}
