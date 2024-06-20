package com.example.aurora.Bean;


import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;


public class Message2 implements Serializable {

    private String idMensaje;

    private String idChat;

    private String mensaje;
    private String senderId;

    private String receiverId;
    private @ServerTimestamp Date fecha;


    public Message2() {
        // Constructor vacío necesario para Firestore
    }

    public Message2(String idMensaje, String idChat, String mensaje, String senderId, String receiverId, Date fecha) {
        this.idMensaje = idMensaje;
        this.idChat = idChat;
        this.mensaje = mensaje;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.fecha = fecha;
    }

    public String getIdMensaje() {
        return idMensaje;
    }

    public void setIdMensaje(String idMensaje) {
        this.idMensaje = idMensaje;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
}



