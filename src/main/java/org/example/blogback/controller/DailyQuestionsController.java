package org.example.blogback.controller;

import org.example.blogback.dto.DailyQuestionDTO;
import org.example.blogback.entity.DailyQuestion;
import org.example.blogback.service.DailyQuestionsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/daily")
public class DailyQuestionsController {
    private final DailyQuestionsService dailyQuestionsService;
    public DailyQuestionsController(DailyQuestionsService dailyQuestionsService) {
        this.dailyQuestionsService = dailyQuestionsService;
    }
    @PostMapping("/addDailyQuestion")
    public DailyQuestion addDailyQuestion(@RequestBody DailyQuestion dailyQuestion) {
        return dailyQuestionsService.addDailyQuestion(dailyQuestion);
    }
    @GetMapping("/question")
    public List<DailyQuestionDTO> getDailyQuestions() {
        return dailyQuestionsService.getDailyQuestionsWithUsernames();
    }

    @GetMapping("/question/{id}")
    public DailyQuestionDTO getDailyQuestionById(@PathVariable Long id) {
        return dailyQuestionsService.getDailyQuestionById(id);
    }


}
