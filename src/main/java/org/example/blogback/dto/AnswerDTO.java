package org.example.blogback.dto;

import java.time.LocalDateTime;

public class AnswerDTO {
    private Long id;
    private String answer;
    private LocalDateTime createdAt;
    private String username;
    private String authorId;

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }





    public AnswerDTO(){

    }
    public AnswerDTO(Long id, String answer, LocalDateTime createdAt, String username) {
        this.id = id;
        this.answer = answer;
        this.createdAt = createdAt;
        this.username = username;

    }


}
