package org.example.blogback.repository;


import org.example.blogback.entity.Saved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedRepository extends JpaRepository<Saved, Long> {
    Optional<Saved> findByUserIdAndPostId(Long userId, Long postId);


    List<Saved> findAllByUser_Id(Long id);
}
