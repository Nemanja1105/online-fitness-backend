package org.unibl.etf.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.unibl.etf.models.dto.CategoryDTO;
import org.unibl.etf.models.dto.CategorySubscribeDTO;
import org.unibl.etf.services.CategoryService;
import org.unibl.etf.services.CategorySubscribeService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")

public class CategoryController {

    private final CategoryService categoryService;
    private final CategorySubscribeService categorySubscribeService;

    public CategoryController(CategoryService categoryService, CategorySubscribeService categorySubscribeService) {
        this.categoryService = categoryService;
        this.categorySubscribeService = categorySubscribeService;
    }

    @GetMapping
    public List<CategoryDTO> findAll(){
        return this.categoryService.findAll();
    }

    @GetMapping("/subscriptions/{clientId}")
    public List<CategorySubscribeDTO> findAllForClient(@PathVariable Long clientId, Authentication auth){
        return this.categorySubscribeService.findAllForClient(clientId,auth);
    }

    @PutMapping("/{categoryId}/subscriptions/{clientId}")
    public void changeSubForClient(@PathVariable Long categoryId,@PathVariable Long clientId,Authentication auth){
        this.categorySubscribeService.changeSubscribeForClient(categoryId,clientId,auth);
    }
}
