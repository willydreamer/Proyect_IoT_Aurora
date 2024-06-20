package com.example.aurora.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Chat2 implements Serializable {

    private String idChat;

    private ArrayList<String> listaMensajes;

    private String usuario1;
    private String usuario2;

    public Chat2() {
        // Constructor vacío requerido por Firestore
    }

    public Chat2(String idChat, ArrayList<String> listaMensajes, String usuario1, String usuario2) {
        this.idChat = idChat;
        this.listaMensajes = listaMensajes;
        this.usuario1 = usuario1;
        this.usuario2 = usuario2;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String idChat) {
        this.idChat = idChat;
    }

    public ArrayList<String> getListaMensajes() {
        return listaMensajes;
    }

    public void setListaMensajes(ArrayList<String> listaMensajes) {
        this.listaMensajes = listaMensajes;
    }

    public String getUsuario1() {
        return usuario1;
    }

    public void setUsuario1(String usuario1) {
        this.usuario1 = usuario1;
    }

    public String getUsuario2() {
        return usuario2;
    }

    public void setUsuario2(String usuario2) {
        this.usuario2 = usuario2;
    }
}

