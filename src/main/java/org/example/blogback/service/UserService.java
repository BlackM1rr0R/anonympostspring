package org.example.blogback.service;

import org.example.blogback.dto.JwtResponse;
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
        // Giriş kontrolü (şifre doğru mu vs.) — Spring yapar
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Kullanıcıyı bul
        Users users = userRepository.findByUsername(user.getUsername());
        if (users == null) {
            throw new RuntimeException("User not found");
        }

        // Token üret
        String token = jwtUtil.generateToken(users.getUsername());

        // DTO olarak dön
        return new JwtResponse(
                token,
                users.getUsername(),
                users.getRoles().name()
        );
    }


    public Users getMyProfile() {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        Users user=userRepository.findByUsername(username);
        if(user==null) {
            throw new RuntimeException("User not found");
        }
        return user;
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
