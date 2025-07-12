package org.example.blogback.repository;

import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Post> searchByTitle(@Param("title") String title);
    List<Post> findByUser(Users user);
}
