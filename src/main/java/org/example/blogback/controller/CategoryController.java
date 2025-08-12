package org.example.blogback.controller;

import org.example.blogback.entity.Category;
import org.example.blogback.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
    @GetMapping("/all-category")
    public List<Category> getAllCategory() {
        return categoryService.getAllCategory();
    }
    @PostMapping("/add-category")
    public Category addCategory(@RequestBody Category category) {
        return categoryService.addCategory(category);
    }

}
