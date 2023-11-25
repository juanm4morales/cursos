package com.challenge.cursos.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.challenge.cursos.models.Alumno;

@Repository
public interface AlumnoRepositorio extends UsuarioRepositorio<Alumno, Long> {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM Alumno a WHERE a.legajo = :legajo")
    boolean existsByLegajo(@Param("legajo") Integer legajo);
}