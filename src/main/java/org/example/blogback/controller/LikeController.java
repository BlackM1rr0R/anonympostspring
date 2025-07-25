package org.example.blogback.controller;

import org.example.blogback.entity.Like;
import org.example.blogback.service.LikeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;
    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @PostMapping("/toggle")
    public boolean toggleLike(@RequestBody Like like) {
        return likeService.toggleLike(like);
    }
    @GetMapping("/post/{postId}/count")
    public Long getLikeCount(@PathVariable Long postId) {
        return likeService.getLikeCount(postId);
    }
    @GetMapping("/post/{postId}")
    public List<Like> getLike(@PathVariable Long postId) {
        return likeService.getLike(postId);
    }
}
