package org.example.blogback.repository;


import org.example.blogback.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);


    boolean existsByUsername(String username);
}
