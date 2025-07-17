package org.example.blogback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class UserProfileResponse {
    private String username;
    private String role;
    private String ip;

    public UserProfileResponse() {}

    public UserProfileResponse(String username, String role, String ip) {
        this.username = username;
        this.role = role;
        this.ip = ip;
    }
    public String getUsername() {
        return username;
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
