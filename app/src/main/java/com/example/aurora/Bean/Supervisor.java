package com.example.aurora.Bean;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


public class Supervisor implements Serializable {


    private Integer idSupervisor;
    //real:
    //private Integer idUsuario;

    private String nombre;
    private String apellido;
    private String dni;

    private String correo;

    private String telefono;

    private String domicilio;

    //private String foto;
    //private Integer tipoDeUsuario;


    //constructor
    public Supervisor(Integer idSupervisor,String nombre,
                      String apellido,String dni, String correo,
                      String telefono , String domicilio){
        this.idSupervisor = idSupervisor;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
        this.telefono = telefono;
        this.domicilio = domicilio;

    }

    public Integer getIdSupervisor() {
        return idSupervisor;
    }

    public void setIdSupervisor(Integer idSupervisor) {
        this.idSupervisor = idSupervisor;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }
}
