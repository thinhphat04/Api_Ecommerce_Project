package com.phat.api_flutter.ChatApp.Controller;

import com.phat.api_flutter.ChatApp.Entity.ChatNotifications;
import com.phat.api_flutter.ChatApp.Repository.ChatMessageRepository;
import com.phat.api_flutter.ChatApp.Entity.ChatMessage;
import com.phat.api_flutter.ChatApp.Services.ChatMessageServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ChatController {
    // Inject SimpMessagingTemplate để gửi tin nhắn thời gian thực
    private final SimpMessagingTemplate messagingTemplate;

    // Inject ChatMessageServices để quản lý các thao tác với ChatMessage
    private final ChatMessageServices chatMessageServices;

    // Xử lý tin nhắn gửi đến đích "/chat"
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage) {

        ChatMessage savedMsg = chatMessageServices.save(chatMessage);

        // Gửi thông báo đến người nhận qua WebSocket
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId(),
                "/queue/messages",
                ChatNotifications.builder()
                        .id(savedMsg.getId())
                        .senderId(savedMsg.getSenderId())
                        .recipientId(savedMsg.getRecipientId())
                        .content(savedMsg.getContent())
                        .build()
        );
        System.out.println("Received message: " + chatMessage);
    }


    // Xử lý yêu cầu GET để lấy danh sách tin nhắn giữa hai người dùng
    @GetMapping("/api/v1/messages/{senderId}/{recipientId}")
    public ResponseEntity<List<ChatMessage>> findChatMessage(@PathVariable("senderId") String senderId,
                                                             @PathVariable("recipientId") String recipientId) {
        List<ChatMessage> messages = chatMessageServices.findChatMessages(senderId, recipientId);
        for (int k = 0, j = messages.size() - 1; k < j; k++)
        {
            messages.add(k, messages.remove(j));
        }
        // Trả về danh sách các tin nhắn giữa senderId và recipientId dưới dạng HTTP response
        return ResponseEntity.ok(messages);
    }
}
