package org.example.blogback.service;

import lombok.RequiredArgsConstructor;
import org.example.blogback.dto.ConversationSummary;
import org.example.blogback.entity.ChatMessage;
import org.example.blogback.repository.ChatMessageRepository;
import org.example.blogback.jwt.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final JwtUtil jwtUtil;

    public ChatMessage saveMessage(ChatMessage message) {
        // ✔️ Gizli boşluklara karşı koruma
        if (message.getReceiver() != null) {
            message.setReceiver(message.getReceiver().trim());
        }
        if (message.getSender() != null) {
            message.setSender(message.getSender().trim());
        }
        return chatMessageRepository.save(message);
    }

    public String getCurrentUserFromToken(String token) {
        return jwtUtil.getUsernameFromToken(token);
    }

    // ✔️ Çift yönlü geçmiş
    public List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) {
        String u1 = user1 == null ? null : user1.trim();
        String u2 = user2 == null ? null : user2.trim();
        return chatMessageRepository.findConversation(u1, u2);
    }

    public List<ConversationSummary> getConversationsForUser(String user) {
        return chatMessageRepository.findConversationPartners(user).stream()
                .map(r -> new ConversationSummary(r.getPartner(), r.getLastTime()))
                .toList();
    }
}

