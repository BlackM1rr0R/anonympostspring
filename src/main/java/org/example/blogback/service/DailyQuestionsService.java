package org.example.blogback.service;

import org.example.blogback.dto.AnswerDTO;
import org.example.blogback.dto.DailyQuestionDTO;
import org.example.blogback.entity.DailyQuestion;
import org.example.blogback.repository.DailyQuestionsRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyQuestionsService {
    private final DailyQuestionsRepository dailyQuestionsRepository;
    public DailyQuestionsService(DailyQuestionsRepository dailyQuestionsRepository) {
        this.dailyQuestionsRepository = dailyQuestionsRepository;
    }
    public DailyQuestion addDailyQuestion(DailyQuestion dailyQuestion) {
        return dailyQuestionsRepository.save(dailyQuestion);
    }

    public List<DailyQuestion> getDailyQuestions() {
        return dailyQuestionsRepository.findAll();
    }

    public List<DailyQuestionDTO> getDailyQuestionsWithUsernames() {
        return dailyQuestionsRepository.findAll().stream().map(question -> {
            List<AnswerDTO> answerDTOs = question.getAnswers().stream()
                    .map(answer -> new AnswerDTO(
                            answer.getId(),
                            answer.getAnswer(),
                            answer.getCreatedAt(),
                            answer.getUser().getUsername()
                    ))
                    .toList();
            return new DailyQuestionDTO(
                    question.getId(),
                    question.getQuestion(),
                    question.getCreatedAt(),
                    answerDTOs
            );
        }).toList();
    }

    public DailyQuestionDTO getDailyQuestionById(Long id) {
        DailyQuestion dailyQuestion = dailyQuestionsRepository.findById(id).orElse(null);
        List<AnswerDTO> answerDTOs=dailyQuestion.getAnswers().stream()
                .map(answer-> new AnswerDTO(
                        answer.getId(),
                        answer.getAnswer(),
                        answer.getCreatedAt(),
                        answer.getUser().getUsername()

                ))
                .toList();
        return new DailyQuestionDTO(
                dailyQuestion.getId(),
                dailyQuestion.getQuestion(),
                dailyQuestion.getCreatedAt(),
                answerDTOs
        );
    }
}
