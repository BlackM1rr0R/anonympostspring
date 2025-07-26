package org.example.blogback.repository;

import org.example.blogback.entity.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionsAnswerRepository extends JpaRepository<QuestionAnswer,Long> {
}
