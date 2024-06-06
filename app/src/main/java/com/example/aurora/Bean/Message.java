package com.example.aurora.Bean;

import java.io.Serializable;

public class Message implements Serializable {

    private String senderId;
    private String receiverId;
    private String message;
    private long timestamp;

    public Message() {
        // Constructor vacío requerido por Firestore
    }

    public Message(String senderId, String receiverId, String message, long timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
