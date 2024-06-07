package com.example.aurora.Bean;

import java.io.Serializable;

public class Chat implements Serializable {

    private String user1;
    private Message message;
    private String user2;

    public Chat() {
        // Constructor vacío requerido por Firestore
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public Chat(String user1, Message message, String user2) {
        this.user1 = user1;
        this.message = message;
        this.user2 = user2;
    }

}
