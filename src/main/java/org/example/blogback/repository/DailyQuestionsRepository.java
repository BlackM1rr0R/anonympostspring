package org.example.blogback.repository;

import org.example.blogback.entity.DailyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyQuestionsRepository extends JpaRepository<DailyQuestion,Long> {
}
