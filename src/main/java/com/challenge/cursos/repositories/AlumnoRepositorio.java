package com.challenge.cursos.repositories;

import org.springframework.stereotype.Repository;

import com.challenge.cursos.models.Alumno;

@Repository
public interface AlumnoRepositorio extends UsuarioRepositorio<Alumno, Long> {

    
}