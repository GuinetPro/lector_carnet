package com.poch.asynctask.pdf417.models;

import java.util.Date;

/**
 * Created by ricardo.gutierrez on 21-01-2016.
 */
public class Visita {

    private String id;
    private  String nombre;
    private String  rut;


    public Visita(String nombre, String rut) {
        this.nombre = nombre;
        this.rut = rut;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
