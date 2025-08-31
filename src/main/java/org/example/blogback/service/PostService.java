package org.example.blogback.service;

import org.example.blogback.dto.PostResponse;
import org.example.blogback.entity.Category;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.exception.PostException;
import org.example.blogback.repository.CategoryRepository;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    public Post addPost(String title, String content, String author, MultipartFile image, Long categoryId) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setUser(user);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        post.setCategory(category);

        if (image != null && !image.isEmpty()) {
            String uploadDir = "uploads/";
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());
            post.setImageUrl("/uploads/" + fileName);
        }

        return postRepository.save(post);
    }

    public Page<Post> getAllPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Post editPost(Post post) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();

        Post existingPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!existingPost.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You have not role");
        }

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setAuthor(post.getAuthor());
        return postRepository.save(existingPost);
    }

    public List<PostResponse> getMyPosts() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("You have not role");
        }

        return postRepository.findByUser(user).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<PostResponse> searchByTitle(String title) {
        return postRepository.searchByTitle(title).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostException("Post not found " + id));
        return mapToDto(post);
    }

    public String deleteAllPosts() {
        postRepository.deleteAll();
        return "Bütün postlar silindi";
    }

    public List<PostResponse> getPostsByCategoryId(Long categoryId) {
        return postRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public PostResponse mapToDto(Post post) {
        PostResponse dto = new PostResponse();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setAuthor(post.getAuthor());
        dto.setAuthorId(post.getUser() != null ? post.getUser().getId() : null);
        dto.setContent(post.getContent());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setImageUrl(post.getImageUrl());
        dto.setCategoryName(post.getCategory() != null ? post.getCategory().getCategoryName() : null);
        return dto;
    }
}
