package com.chatground.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatgroundMessage {
    private String type; //enum:{chat, onOpen, onClose}
    private String sender;
    private String senderNickname;
    private String message;
    private Integer onlineCounter;
}
