package com.challenge.cursos.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "profesores")
public class Profesor extends Usuario {
    
    private String especialidad;

    private String telefono;

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
}
