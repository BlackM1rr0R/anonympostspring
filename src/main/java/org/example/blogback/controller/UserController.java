package org.example.blogback.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.blogback.dto.UserProfileResponse;
import org.example.blogback.entity.Users;
import org.example.blogback.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        return ResponseEntity.ok(userService.login(user));
    }

    @GetMapping("/allusers")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }
    @GetMapping("/my-profile")
    public UserProfileResponse getMyProfile(HttpServletRequest request) {
        return userService.getMyProfile(request);
    }

    @PutMapping("/edit-profile")
    public Users editProfile(@RequestBody Users user) {
        return userService.editProfile(user);
    }
}
