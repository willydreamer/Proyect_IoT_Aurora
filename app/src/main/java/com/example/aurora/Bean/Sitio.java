package com.example.aurora.Bean;

import java.io.Serializable;

public class Sitio implements Serializable {

    private Integer idSitio;

    private String departamento;
    private String provincia;
    private String distrito;

    private String tipoDeZona;

    private String tipoDeSitio;

    public Sitio(String idSitio,String departamento,
                 String provincia,String distrito, String tipoDeZona,
                 String tipoDeSitio ){

    }

    public Integer getIdSitio() {
        return idSitio;
    }

    public void setIdSitio(Integer idSitio) {
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

    public String getTipoDeSitio() {
        return tipoDeSitio;
    }

    public void setTipoDeSitio(String tipoDeSitio) {
        this.tipoDeSitio = tipoDeSitio;
    }
}
