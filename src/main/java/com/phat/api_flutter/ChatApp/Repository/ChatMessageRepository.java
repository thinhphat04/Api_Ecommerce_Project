package com.phat.api_flutter.ChatApp.Repository;

import com.phat.api_flutter.ChatApp.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String>{
    List<ChatMessage> findByChatId(String s);
}
