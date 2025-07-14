package org.example.blogback.repository;

import org.example.blogback.entity.Like;
import org.example.blogback.entity.Post;
import org.example.blogback.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    List<Like> findAllByPostId(Long postId);

    Optional<Like> findByUserAndPost(Users user, Post post);
}
