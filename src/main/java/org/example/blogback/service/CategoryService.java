package org.example.blogback.service;

import org.example.blogback.entity.Category;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.CategoryRepository;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.example.blogback.role.UserRoles;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository,PostRepository postRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if(user.getRoles() != UserRoles.ADMIN){
            throw new RuntimeException("You are not allowed to add this category");
        }
        return categoryRepository.save(category);
    }



}
