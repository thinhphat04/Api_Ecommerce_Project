package com.phat.api_flutter.ChatApp.Services;

import com.phat.api_flutter.ChatApp.Entity.ChatMessage;
import com.phat.api_flutter.ChatApp.Repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageServices {
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomServices chatRoomServices;
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomServices.getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true).orElseThrow();
        chatMessage.setChatId(chatId);
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    public List<ChatMessage> findChatMessages(String senderId, String recipientId) {
        var chatId = chatRoomServices.getChatRoomId(senderId, recipientId, false);
        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }
}
