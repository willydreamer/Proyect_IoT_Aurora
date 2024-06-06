package com.example.aurora.Bean;

import java.io.Serializable;

public class Chat implements Serializable {

    private String chatId;
    private String chatName;
    private String receiverId;

    public Chat() {
        // Constructor vacío requerido por Firestore
    }

    public Chat(String chatId, String chatName, String receiverId) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.receiverId = receiverId;
    }

    public String getChatId() {
        return chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public String getReceiverId() {
        return receiverId;
    }
}
