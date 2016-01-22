package com.poch.asynctask.pdf417.models;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by ricardo.gutierrez on 18-01-2016.
 */
public class Bitacora extends RealmObject {

    private String id;
    private Date fechaIngreso;
    private Date fechaSalida;
    private  String motivo;
    private int personaId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPersonaId() {
        return personaId;
    }

    public void setPersonaId(int personaId) {
        this.personaId = personaId;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }
}
