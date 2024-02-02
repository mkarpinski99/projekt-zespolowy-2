package com.blynder.blynder.chat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessage {
    private String message;
    private String username;
    private String room;

    public ChatMessage() {
    }
}
