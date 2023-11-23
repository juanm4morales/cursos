package com.challenge.cursos.models;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "alumnos",
uniqueConstraints = {
    @UniqueConstraint(
        name="restriccion_legajo",
        columnNames = "legajo"
    )
})
public class Alumno extends Usuario {
    @NotNull(message = "El legajo no debe ser nulo")
    
    private Integer legajo;

    public Integer getLegajo() {
        return legajo;
    }

    public void setLegajo(Integer legajo) {
        this.legajo = legajo;
    }
        
    
}
