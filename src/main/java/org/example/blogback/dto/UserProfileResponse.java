package org.example.blogback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.blogback.entity.Post;

import java.util.List;

@Data
public class UserProfileResponse {
    private Long id;
    private String username;
    private String role;
    private String ip;
    private String password;
    private List<Post> posts;
    public UserProfileResponse() {
    }

    public UserProfileResponse(String username, String role, String ip, String password) {
        this.username = username;
        this.role = role;
        this.ip = ip;
        this.password = password;
    }

    public UserProfileResponse(String username, List<Post> posts) {
        this.username = username;
        this.posts = posts;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


}
