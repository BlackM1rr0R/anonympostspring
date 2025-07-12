package org.example.blogback.service;

import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
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

@Service
public class PostService {
    private PostRepository postRepository;
    private UserRepository userRepository;
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }
    public Post addPost(String title, String content, String author, MultipartFile image) throws IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        post.setUser(user);

        if (image != null && !image.isEmpty()) {
            String uploadDir = "src/main/resources/static/uploads/";
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + fileName);
            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, image.getBytes());

            post.setImageUrl("/uploads/" + fileName);
        }

        return postRepository.save(post);
    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }


    public Post editPost(Post post) {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String currentUsername=auth.getName();
        Post existingPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("Post not found"));
        if(!existingPost.getUser().getUsername().equals(currentUsername)) {
            throw new RuntimeException("You have not role");
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setAuthor(post.getAuthor());
        return postRepository.save(existingPost);
    }

    public List<Post> getMyPosts() {
        Authentication auth=SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user=userRepository.findByUsername(username);
        if(user==null) {
            throw new RuntimeException("You have not role");
        }
        return postRepository.findByUser(user);
    }

    public List<Post> searchByTitle(String title) {
        return postRepository.searchByTitle(title);
    }

    public Post getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        return post;
    }
}
