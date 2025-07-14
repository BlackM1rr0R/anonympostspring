package org.example.blogback.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "all_comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;
    @ManyToOne
    @JsonBackReference(value = "post-comment")
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne
    @JsonBackReference(value = "user-comment")
    @JoinColumn(name = "user_id")
    private Users user;
    @JsonProperty("username")
    public String getUsername() {
        return user != null ? user.getUsername() : null;
    }















    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }


}
