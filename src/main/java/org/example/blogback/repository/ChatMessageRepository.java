package org.example.blogback.repository;

import org.example.blogback.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // İKİ KULLANICI ARASINDAKİ MESAJLAR (yukarıdan aşağı zaman sıralı)
    @Query("""
           select m from ChatMessage m
           where (m.sender = :u1 and m.receiver = :u2)
              or (m.sender = :u2 and m.receiver = :u1)
           order by m.timestamp asc
           """)
    List<ChatMessage> findConversation(@Param("u1") String u1, @Param("u2") String u2);

    // Aynı sorgunun sayfalanan (paged) versiyonu (opsiyonel, istersen kullan)
    @Query("""
           select m from ChatMessage m
           where (m.sender = :u1 and m.receiver = :u2)
              or (m.sender = :u2 and m.receiver = :u1)
           order by m.timestamp desc
           """)
    Page<ChatMessage> findConversationPage(@Param("u1") String u1, @Param("u2") String u2, Pageable pageable);

    // KONUŞTUĞUN TÜM PARTNERLER (her partner için 1 satır) + SON MESAJ ZAMANI
    @Query("""
           select 
             case when m.sender = :user then m.receiver else m.sender end as partner,
             max(m.timestamp) as lastTime
           from ChatMessage m
           where m.sender = :user or m.receiver = :user
           group by case when m.sender = :user then m.receiver else m.sender end
           order by max(m.timestamp) desc
           """)
    List<ConversationRow> findConversationPartners(@Param("user") String user);

    // Projection: partner ve son zaman alanlarını tipli döndürmek için
    interface ConversationRow {
        String getPartner();
        LocalDateTime getLastTime();
    }
}
