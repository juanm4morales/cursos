package com.challenge.cursos.repositories;

import org.springframework.stereotype.Repository;

import com.challenge.cursos.models.Profesor;

@Repository
public interface ProfesorRepositorio extends UsuarioRepositorio<Profesor, Long> {

    
}