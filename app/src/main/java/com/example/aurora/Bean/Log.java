package com.example.aurora.Bean;

import java.io.Serializable;

public class Log implements Serializable {

    private Integer idLog;

    private String fecha;

    private String actividad;

    private String description;

    private Usuario usuario;

    private Sitio sitio;



    public Log(Integer idLog, String fecha, String actividad, String description, Usuario usuario, Sitio sitio){
        this.idLog = idLog;
        this.fecha = fecha;
        this.actividad = actividad;
        this.description = description;
        this.usuario = usuario;
        this.sitio = sitio;
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getActividad() {
        return actividad;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Sitio getSitio() {
        return sitio;
    }

    public void setSitio(Sitio sitio) {
        this.sitio = sitio;
    }
}
