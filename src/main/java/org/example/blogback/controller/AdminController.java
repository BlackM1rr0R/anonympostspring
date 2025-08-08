package org.example.blogback.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.blogback.dto.UserProfileResponse;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.PostRepository;
import org.example.blogback.repository.UserRepository;
import org.example.blogback.role.UserRoles;
import org.example.blogback.service.PostService;
import org.example.blogback.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public AdminController(UserService userService, PostRepository postRepository, UserRepository userRepository) {
        this.userService = userService;
       this.postRepository = postRepository;
       this.userRepository = userRepository;
    }
    @GetMapping("/allUsers")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
    @PutMapping("/edit-admin-profile")
    public void editForAdmin(@RequestBody UserProfileResponse userDto) {
        userService.editForAdmin(userDto);
    }
    @GetMapping("/all-post-for-admin")
    public List<Post> getAllPostForAdmin() {
        return postRepository.findAll();
    }
    @DeleteMapping("/delete-post/{id}")
    public void deletePost(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
    @PutMapping("/edit-post/{id}")
    public void editPost(@PathVariable Long id, @RequestBody Post postDto) {
        Post existingPost = postRepository.findById(id).get();
        existingPost.setTitle(postDto.getTitle());
        existingPost.setContent(postDto.getContent());
        postRepository.saveAndFlush(existingPost);
    }
    @PostMapping("/add-new-user")
    public void addNewUser(@RequestBody UserProfileResponse userDto, HttpServletRequest request) {
        Users user = new Users();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setRoles(UserRoles.valueOf(userDto.getRole()));
        String ip=request.getHeader("x-forwarded-for");
        if (ip == null || ip.isEmpty()) {
            ip=request.getRemoteAddr();
        }
        user.setIpAddress(ip);
        userRepository.save(user);
    }


}
