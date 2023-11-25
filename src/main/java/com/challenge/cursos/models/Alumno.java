package com.challenge.cursos.models;


import com.challenge.cursos.validators.LegajoUnico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "alumnos")
public class Alumno extends Usuario {
    @NotNull(message = "El legajo no debe ser nulo")
    @Column(unique = true) @LegajoUnico

    private Integer legajo;

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }
        
    
}
