package org.example.blogback.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // WebSocket CONNECT isteği
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String token = accessor.getFirstNativeHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                if (jwtUtil.validateJwtToken(token)) {
                    String username = jwtUtil.getUsernameFromToken(token);

                    // ✅ Principal ayarlanıyor
                    accessor.setUser((Principal) () -> username);
                } else {
                    throw new IllegalArgumentException("❌ Geçersiz JWT token!");
                }
            } else {
                throw new IllegalArgumentException("❌ Authorization header eksik!");
            }
        }

        return message;
    }
}
