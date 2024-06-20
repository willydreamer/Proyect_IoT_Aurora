package com.example.aurora.Bean;

import java.io.Serializable;

public class Message implements Serializable {

    private String user;
    private int idmessage ;
    private String timestamp;
    private String content;

    public Message() {
        // Constructor vacío requerido por Firestore
    }

    public Message(String user, int idmessage, String timestamp, String  content) {
        this.user = user;
        this.idmessage = idmessage;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIdmessage() {
        return idmessage;
    }

    public void setIdmessage(int idmessage) {
        this.idmessage = idmessage;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}