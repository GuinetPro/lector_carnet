package com.poch.asynctask.pdf417.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by ricardo.gutierrez on 25-01-2016.
 */
public class VisitaRealm extends RealmObject {


    @PrimaryKey
    private String id;
    private  String nombre;

    private String  rut;

    private String bitacoraId;


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

    public String getBitacoraId() {
        return bitacoraId;
    }

    public void setBitacoraId(String bitacoraId) {
        this.bitacoraId = bitacoraId;
    }
}
