package org.example.blogback.controller;

import org.example.blogback.dto.AnswerDTO;
import org.example.blogback.entity.QuestionAnswer;
import org.example.blogback.service.QuestionsAnswerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/answer")
public class QuestionsAnswerController {
    private final QuestionsAnswerService questionsAnswerService;
    public QuestionsAnswerController(QuestionsAnswerService questionsAnswerService) {
        this.questionsAnswerService = questionsAnswerService;
    }

    @PostMapping("/add")
    public QuestionAnswer addAnswer(@RequestBody QuestionAnswer questionAnswer) {
        return questionsAnswerService.addAnswer(questionAnswer);
    }

    @GetMapping("/allAnswer")
    public List<AnswerDTO> getAllAnswer() {
        return questionsAnswerService.getAllAnswer();
    }

}
