package org.example.blogback.controller;

import org.example.blogback.entity.Comment;
import org.example.blogback.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {
    private final CommentService commentService;
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add")
    public Comment addComment(@RequestBody Comment comment) {
        return commentService.addComment(comment);
    }

    @GetMapping("/post/{postId}")
    public Comment getComment(@PathVariable Long postId) {
        return commentService.getComment(postId);
    }
}
