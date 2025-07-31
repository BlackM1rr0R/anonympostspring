package org.example.blogback.controller;

import org.example.blogback.dto.UserProfileResponse;
import org.example.blogback.entity.Users;
import org.example.blogback.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    public AdminController(UserService userService) {
        this.userService = userService;
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
}
