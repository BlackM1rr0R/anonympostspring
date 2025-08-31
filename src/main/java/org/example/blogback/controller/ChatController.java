package org.example.blogback.controller;

import lombok.RequiredArgsConstructor;
import org.example.blogback.dto.ConversationSummary;
import org.example.blogback.entity.ChatMessage;
import org.example.blogback.service.ChatService;
import org.example.blogback.jwt.JwtUtil;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;
    private final JwtUtil jwtUtil;

    private String resolveSender(SimpMessageHeaderAccessor accessor, Principal principal) {
        if (principal != null) return principal.getName();
        if (accessor.getUser() != null) return accessor.getUser().getName();
        String auth = accessor.getFirstNativeHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return jwtUtil.getUsernameFromToken(auth.substring(7));
        }
        return null;
    }

    @MessageMapping("/private-message")
    public void sendPrivateMessage(@Payload ChatMessage message,
                                   SimpMessageHeaderAccessor accessor,
                                   Principal principal) {

        String sender = resolveSender(accessor, principal);
        if (sender == null) {
            throw new RuntimeException("Kullanıcı doğrulanamadı (sender null).");
        }

        // ⚠️ İstemciden gelen sender'ı YOK SAY
        message.setSender(sender);
        message.setTimestamp(LocalDateTime.now());

        ChatMessage saved = chatService.saveMessage(message);

        // Alıcıya ve gönderenin kendisine düşür
        messagingTemplate.convertAndSendToUser(message.getReceiver(), "/queue/messages", saved);
        messagingTemplate.convertAndSendToUser(sender, "/queue/messages", saved);
    }

    @GetMapping("/messages/{receiver}")
    public List<ChatMessage> getMessages(@PathVariable String receiver,
                                         @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String currentUser = chatService.getCurrentUserFromToken(token);

        // 🔎 Hızlı teşhis için:
        System.out.println("History -> currentUser='" + currentUser + "', other='" + receiver + "'");

        return chatService.getMessagesBetweenUsers(currentUser, receiver);
    }

    @GetMapping("/conversations")
    public List<ConversationSummary> getConversations(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String currentUser = chatService.getCurrentUserFromToken(token);
        return chatService.getConversationsForUser(currentUser);
    }

}
