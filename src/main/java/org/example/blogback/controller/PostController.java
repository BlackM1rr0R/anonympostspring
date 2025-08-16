package org.example.blogback.controller;

import org.example.blogback.dto.PostResponse;
import org.example.blogback.entity.Post;
import org.example.blogback.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Post addPost(@RequestParam("title") String title,
                        @RequestParam("content") String content,
                        @RequestParam("author") String author,
                        @RequestParam(value = "image", required = false) MultipartFile image,
                        @RequestParam("category") Long categoryId) throws IOException {
        return postService.addPost(title, content, author, image, categoryId);
    }

    @PutMapping("/edit")
    public Post editPost(@RequestBody Post post) {
        return postService.editPost(post);
    }

    @GetMapping("/my-posts")
    public List<PostResponse> getMyPosts() {
        return postService.getMyPosts();
    }

    @GetMapping("/all")
    public Page<PostResponse> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ){
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return postService.getAllPosts(pageable).map(postService::mapToDto);
    }

    @GetMapping("/search")
    public List<PostResponse> searchByTitle(@RequestParam("title") String title){
        return postService.searchByTitle(title);
    }

    @GetMapping("/search/post/{id}")
    public PostResponse getPostById(@PathVariable Long id){
        return postService.getPostById(id);
    }

    @DeleteMapping("/delete/all")
    public String deleteAllPosts(){
        return postService.deleteAllPosts();
    }

    @GetMapping("/category/{id}")
    public List<PostResponse> getPostsByCategory(@PathVariable Long id) {
        return postService.getPostsByCategoryId(id);
    }


}
