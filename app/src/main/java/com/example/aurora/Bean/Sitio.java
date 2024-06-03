package com.example.aurora.Bean;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;

public class Sitio implements Serializable {

    private String idSitio;
    private String departamento;
    private String provincia;
    private String distrito;
    private String tipoDeZona;
    private String latitud;
    private String longitud;
    private String operadora;
    //private String encargado;


    private ArrayList<Usuario> supervisor;

    //private List<Equipo> equipos;

    public Sitio() {
    }

    public Sitio(String idSitio, String departamento, String provincia, String distrito, String tipoDeZona, String latitud, String longitud, String operadora,  ArrayList<Usuario> supervisor) {
        this.idSitio = idSitio;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
        this.tipoDeZona = tipoDeZona;
        this.latitud = latitud;
        this.longitud = longitud;
        this.operadora = operadora;
        this.supervisor = supervisor;
        //this.encargado = encargado;
    }

    /*public String getEncargado() {
        return encargado;
    }

    public void setEncargado(String encargado) {
        this.encargado = encargado;
    }*/

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getOperadora() {
        return operadora;
    }

    public void setOperadora(String operadora) {
        this.operadora = operadora;
    }

    public String getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(String idSitio) {
        this.idSitio = idSitio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getTipoDeZona() {
        return tipoDeZona;
    }

    public void setTipoDeZona(String tipoDeZona) {
        this.tipoDeZona = tipoDeZona;
    }

    //@Exclude
    public ArrayList<Usuario> getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(ArrayList<Usuario> supervisor) {
        this.supervisor = supervisor;
    }
}
