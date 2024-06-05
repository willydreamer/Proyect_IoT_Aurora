package com.example.aurora.Bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Equipo implements Serializable {

    private String idEquipo;
    private String SKU;
    private String numeroDeSerie;

    private String tipoDeEquipo;
    private String marca;
    private String modelo;
    private String descripcion;
    private Date fechaDeRegistro;
    private String fotoEquipo;

    private ArrayList<String> sitios;

    private String estado;

    public Equipo(){

    }

    public Equipo(String idEquipo, String SKUstr, String numeroDeSerieStr, String tipoDeEquipo, String marcaStr, String modeloStr, String descripcionStr, Date fechaDeRegistro, String imageUrl, ArrayList<String> sitios, String estado){
        this.idEquipo = idEquipo;
        this.SKU = SKUstr;
        this.numeroDeSerie = numeroDeSerieStr;
        this.tipoDeEquipo = tipoDeEquipo;
        this.marca = marcaStr;
        this.modelo = modeloStr;
        this.descripcion = descripcionStr;
        this.fechaDeRegistro = fechaDeRegistro;
        this.fotoEquipo = imageUrl;
        this.sitios = sitios;
        this.estado = estado;

    }

   /* public Equipo(String idEquipo, String SKU, String numeroDeSerie, String tipoDeEquipo, String marca, String modelo, String descripcion, Date fechaDeRegistro, String fotoEquipo, ArrayList<String> sitios, String estado) {
        this.idEquipo = idEquipo;
        this.SKU = SKU;
        this.numeroDeSerie = numeroDeSerie;
        this.tipoDeEquipo = tipoDeEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.fechaDeRegistro = fechaDeRegistro;
        this.fotoEquipo = fotoEquipo;
        this.sitios = sitios;
        this.estado = estado;
    }*/





    public String getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(String idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getNumeroDeSerie() {
        return numeroDeSerie;
    }

    public void setNumeroDeSerie(String numeroDeSerie) {
        this.numeroDeSerie = numeroDeSerie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoDeEquipo() {
        return tipoDeEquipo;
    }

    public void setTipoDeEquipo(String tipoDeEquipo) {
        this.tipoDeEquipo = tipoDeEquipo;
    }

    public Date getFechaDeRegistro() {
        return fechaDeRegistro;
    }

    public void setFechaDeRegistro(Date fechaDeRegistro) {
        this.fechaDeRegistro = fechaDeRegistro;
    }

    public ArrayList<String> getSitios() {
        return sitios;
    }

    public void setSitios(ArrayList<String> sitios) {
        this.sitios = sitios;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getFotoEquipo() {
        return fotoEquipo;
    }

    public void setFotoEquipo(String fotoEquipo) {
        this.fotoEquipo = fotoEquipo;
    }
}
