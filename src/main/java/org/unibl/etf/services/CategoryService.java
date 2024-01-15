package org.unibl.etf.services;

import org.unibl.etf.models.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> findAll();
}
