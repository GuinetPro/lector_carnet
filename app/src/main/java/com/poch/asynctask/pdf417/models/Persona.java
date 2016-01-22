package com.poch.asynctask.pdf417.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class Persona extends RealmObject {

    private String id;
    private String nombre;
    private Date cargo;
    private  String area;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getCargo() {
        return cargo;
    }

    public void setCargo(Date cargo) {
        this.cargo = cargo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
