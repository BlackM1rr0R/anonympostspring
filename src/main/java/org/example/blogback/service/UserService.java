package org.example.blogback.service;

import jakarta.servlet.http.HttpServletRequest;
import org.example.blogback.dto.JwtResponse;
import org.example.blogback.dto.UserProfileResponse;
import org.example.blogback.entity.Users;
import org.example.blogback.jwt.JwtUtil;
import org.example.blogback.repository.UserRepository;
import org.example.blogback.role.UserRoles;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public Users register(Users user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setRoles(UserRoles.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public JwtResponse login(Users user) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        Users users = userRepository.findByUsername(user.getUsername());
        if (users == null) {
            throw new RuntimeException("User not found");
        }
        String token = jwtUtil.generateToken(users.getUsername());
        return new JwtResponse(
                token,
                users.getUsername(),
                users.getRoles().name()
        );
    }


    public UserProfileResponse getMyProfile(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }

        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return new UserProfileResponse(user.getUsername(), user.getRoles().name(), ipAddress);
    }


    public Users editProfile(Users user) {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        Users users=userRepository.findByUsername(username);
        if(users==null) {
            throw new RuntimeException("User not found");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
