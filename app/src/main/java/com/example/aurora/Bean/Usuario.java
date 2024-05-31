package com.example.aurora.Bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuario implements Serializable {


    private String idUsuario;

    private String nombre;

    private String apellido;

    private String dni;

    private String correo;

    private String domicilio;

    private String telefono;

    private String rol;

    private String estado;

    private ArrayList<Sitio> sitios;
    //maximo 5 sitios por supervisor
    //1 supervisor por sitio

    public Usuario() {
    }

    public Usuario (String idUsuario, String nombre, String apellido, String dni, String correo, String domicilio, String telefono, String rol, String estado,
                    ArrayList<Sitio> sitios){

        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
        this.correo = correo;
        this.domicilio = domicilio;
        this.telefono = telefono;
        this.rol = rol;
        this.estado = estado;
        this.sitios = sitios;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(String domicilio) {
        this.domicilio = domicilio;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public ArrayList<Sitio> getSitios() {
        return sitios;
    }

    public void setSitios(ArrayList<Sitio> sitios) {
        this.sitios = sitios;
    }
}
