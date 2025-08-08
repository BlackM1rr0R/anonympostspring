package org.example.blogback.service;

import org.example.blogback.dto.AnswerDTO;
import org.example.blogback.entity.DailyQuestion;
import org.example.blogback.entity.QuestionAnswer;
import org.example.blogback.entity.Users;
import org.example.blogback.repository.DailyQuestionsRepository;
import org.example.blogback.repository.QuestionsAnswerRepository;
import org.example.blogback.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionsAnswerService {
    private final QuestionsAnswerRepository questionsAnswerRepository;
    private final UserRepository userRepository;
    private final DailyQuestionsRepository dailyQuestionsRepository;
    public QuestionsAnswerService(QuestionsAnswerRepository questionsAnswerRepository, UserRepository userRepository, DailyQuestionsRepository dailyQuestionsRepository) {
        this.questionsAnswerRepository = questionsAnswerRepository;
        this.userRepository = userRepository;
        this.dailyQuestionsRepository = dailyQuestionsRepository;
    }

    public QuestionAnswer addAnswer(QuestionAnswer questionAnswer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        DailyQuestion dailyQuestion = dailyQuestionsRepository.findById(questionAnswer.getQuestion().getId())
                .orElseThrow(() -> new RuntimeException("DailyQuestion not found"));

        if(user==null) {
            throw new RuntimeException("User not found");
        }
        if(dailyQuestion==null) {
            throw new RuntimeException("DailyQuestion not found");
        }
        QuestionAnswer answer=new QuestionAnswer();
        answer.setUser(user);
        answer.setQuestion(dailyQuestion);
        answer.setAnswer(questionAnswer.getAnswer());
        return questionsAnswerRepository.save(answer);
    }

    public List<AnswerDTO> getAllAnswer() {
        List<QuestionAnswer> answers = questionsAnswerRepository.findAll();

        return answers.stream()
                .map(ans -> new AnswerDTO(
                        ans.getId(),
                        ans.getAnswer(),
                        ans.getCreatedAt(),
                        ans.getUser() != null ? ans.getUser().getUsername() : null
                ))
                .toList();
    }

    public void deleteAnswer(Long answerId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Users user = userRepository.findByUsername(username);
        if(user==null) {
            throw new RuntimeException("User not found");
        }
        QuestionAnswer questionAnswer=questionsAnswerRepository.findById(answerId).orElse(null);
        if(questionAnswer==null) {
            throw new RuntimeException("QuestionAnswer not found");
        }
        if(!questionAnswer.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Answer not belongs to user");
        }

        questionsAnswerRepository.deleteById(answerId);

    }
}
