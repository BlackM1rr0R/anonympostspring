package org.example.blogback.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DailyQuestionDTO {
    private Long id;
    private String question;
    private LocalDateTime createdAt;
    private List<AnswerDTO> answers;

    public DailyQuestionDTO() {

    }
    public DailyQuestionDTO(Long id, String question, LocalDateTime createdAt, List<AnswerDTO> answers) {
        this.id = id;
        this.question = question;
        this.createdAt = createdAt;
        this.answers = answers;
    }
    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }
    public String getQuestion(){
        return question;
    }
    public void setQuestion(String question){
        this.question = question;
    }
    public LocalDateTime getCreatedAt(){
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }
    public List<AnswerDTO> getAnswers(){
        return answers;
    }
    public void setAnswers(List<AnswerDTO> answers){
        this.answers = answers;
    }
}
