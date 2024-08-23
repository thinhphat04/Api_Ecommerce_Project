package com.phat.api_flutter.ChatApp.Repository;

import com.phat.api_flutter.ChatApp.Entity.ChatMessage;
import com.phat.api_flutter.ChatApp.Entity.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String> {
    Optional<ChatRoom> findBySenderIdAndRecipientId(String senderId, String recipientId);
}
