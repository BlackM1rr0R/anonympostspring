package org.example.blogback.dto;

import java.time.LocalDateTime;

public class CommentDTO {
    private Long id;
    private String comment;
    private String username;
    private Long authorId; // EKLENDİ


    public CommentDTO() {}

    public CommentDTO(Long id, String comment, String username, Long authorId) {
        this.id = id;
        this.comment = comment;
        this.username = username;
        this.authorId = authorId;

    }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }


}
