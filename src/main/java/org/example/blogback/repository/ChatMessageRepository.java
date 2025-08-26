package org.example.blogback.repository;

import org.example.blogback.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // ✔️ Çift yön ve doğru parantezle, zamana göre sıralı
    @Query("""
           SELECT m FROM ChatMessage m
           WHERE (m.sender = :u1 AND m.receiver = :u2)
              OR (m.sender = :u2 AND m.receiver = :u1)
           ORDER BY m.timestamp ASC
           """)
    List<ChatMessage> findConversation(@Param("u1") String u1,
                                       @Param("u2") String u2);

    // İstersen diğerleri kalsın ama artık bunu kullanacağız
    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSenderOrderByTimestampAsc(
            String sender1, String receiver1, String sender2, String receiver2
    );

    List<ChatMessage> findBySenderAndReceiverOrReceiverAndSender(String user1, String user2, String user11, String user21);

    List<ChatMessage> findBySenderAndReceiver(String sender, String receiver);
}
