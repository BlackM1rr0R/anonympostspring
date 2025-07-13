package org.example.blogback.service;

import org.example.blogback.entity.Comment;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.CommentRepository;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public Comment addComment(Comment comment) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Users user = userRepository.findByUsername(username);
        Post post = postRepository.findById(comment.getPost().getId()).orElse(null);
        comment.setUser(user);
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public Comment getComment(Long postId) {
        Users user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.getUsername();
    return commentRepository.findById(postId).orElse(null);

    }
}
