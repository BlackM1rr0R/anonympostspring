package org.example.blogback.service;

import org.example.blogback.dto.CommentDTO;
import org.example.blogback.entity.Comment;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.CommentRepository;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public List<CommentDTO> getComment(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        return comments.stream().map(c -> new CommentDTO(
                c.getId(),
                c.getComment(),
                c.getUser().getUsername(),
                c.getUser().getId()
        )).toList();
    }



    public void deleteComment(Long commentId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Comment comment = commentRepository.findById(commentId).orElse(null);
        if (comment == null) {
            throw new RuntimeException("Yorum bulunamadı");
        }
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Bu yorumu silme yetkiniz yok");
        }
        commentRepository.deleteById(commentId);
    }

}
