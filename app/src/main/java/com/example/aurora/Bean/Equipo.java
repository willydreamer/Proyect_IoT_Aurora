package com.example.aurora.Bean;

import java.io.Serializable;

public class Equipo implements Serializable {

    private String claveParaQR;
    private String SKU;
    private String numeroSerie;
    private String marca;
    private String descripcion;
    private String fechaRegistro;
    private String tipoEquipo;

    private String fotos;

    public Equipo(String SKU, String numeroSerie, String marca, String descripcion, String fechaRegistro, String fotos) {
        this.SKU = SKU;
        this.numeroSerie = numeroSerie;
        this.marca = marca;
        this.descripcion = descripcion;
        this.fechaRegistro = fechaRegistro;
        this.fotos = fotos;
    }

    public String getClaveParaQR() {
        return claveParaQR;
    }

    public void setClaveParaQR(String claveParaQR) {
        this.claveParaQR = claveParaQR;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getFotos() {
        return fotos;
    }

    public void setFotos(String fotos) {
        this.fotos = fotos;
    }
}
