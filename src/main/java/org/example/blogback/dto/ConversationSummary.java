package org.example.blogback.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationSummary {
    private String partner;
    private LocalDateTime lastMessageAt;
}
