package org.unibl.etf.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.unibl.etf.models.dto.CategoryDTO;
import org.unibl.etf.services.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")

public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryDTO> findAll(){
        return this.categoryService.findAll();
    }
}
