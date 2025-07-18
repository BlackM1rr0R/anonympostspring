package org.example.blogback.service;

import org.example.blogback.entity.Like;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.LikeRepository;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Like addLike(Like like) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        if (like.getPost() == null || like.getPost().getId() == null) {
            throw new IllegalArgumentException("Post id is null");
        }
        Optional<Post> optionalPost = postRepository.findById(like.getPost().getId());
        if (optionalPost.isEmpty()) {
            throw new IllegalArgumentException("Post id is null");
        }
        Post post = optionalPost.get();
        like.setUser(user);
        like.setPost(post);
        return likeRepository.save(like);
    }

    public List<Like> getLike(Long postId) {
        return likeRepository.findAllByPostId(postId);
    }

    public boolean toggleLike(Like likeRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);

        if (likeRequest.getPost() == null || likeRequest.getPost().getId() == null) {
            throw new IllegalArgumentException("Post id is null");
        }

        Post post = postRepository.findById(likeRequest.getPost().getId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + likeRequest.getPost().getId()));

        Optional<Like> existingLike = likeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            return false;
        } else {
            Like newLike = new Like();
            newLike.setUser(user);
            newLike.setPost(post);
            likeRepository.save(newLike);
            return true;
        }
    }

}
