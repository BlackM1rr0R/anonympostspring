package org.example.blogback.controller;

import org.example.blogback.dto.PostResponse;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Saved;
import org.example.blogback.service.SavedService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/saved")
public class SavedController {
    private final SavedService savedService;
    public SavedController(SavedService savedService) {
        this.savedService = savedService;
    }

    @PostMapping("/toggle")
    public boolean savePost(@RequestBody Saved saved) {
        return savedService.savePost(saved);
    }
    @GetMapping
    public List<PostResponse> getMySaved() {
        return savedService.getCurrentUserSaved();
    }





}
