package com.challenge.cursos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.cursos.exceptions.ExcepcionServicio;
import com.challenge.cursos.models.Profesor;
import com.challenge.cursos.repositories.ProfesorRepositorio;

import jakarta.transaction.Transactional;

@Service
public class ProfesorServicio {

    @Autowired
    ProfesorRepositorio profesorRepositorio;

    @Transactional
    public void crearProfesor(String nombre, String apellido, String email, String especialidad) {
        Profesor profesor = new Profesor();

        profesor.setNombre(nombre);
        profesor.setApellido(apellido);
        profesor.setEmail(email);
        profesor.setEspecialidad(especialidad);
        
        profesorRepositorio.save(profesor);
    }

    @Transactional
    public void crearProfesor(Profesor profesor) {
        profesorRepositorio.save(profesor);
    }

    public List<Profesor> listarProfesores() {
        return (List<Profesor>) profesorRepositorio.findAll();
    }

    public Profesor buscarProfesor(Long idProfesor) throws ExcepcionServicio {
        Optional<Profesor> respuesta = profesorRepositorio.findById(idProfesor);
        Profesor profesor = new Profesor();
        if (respuesta.isPresent()) {
            profesor = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se ha encontrado el profesor con id = " + idProfesor);
        }
        return profesor;
    }

    @Transactional
    public void modificarProfesor(Long idProfesor, String nombre, String apellido, String email, String especialidad) {
        Optional<Profesor> respuesta = profesorRepositorio.findById(idProfesor);

        if (respuesta.isPresent()) {
            Profesor profesor = respuesta.get();

            profesor.setNombre(nombre);
            profesor.setApellido(apellido);
            profesor.setEmail(email);
            profesor.setEspecialidad(especialidad);

            profesorRepositorio.save(profesor);

        }
    }

    @Transactional
    public void eliminarProfesor(Long idProfesor) throws ExcepcionServicio {
        Optional<Profesor> respuesta = profesorRepositorio.findById(idProfesor);

        if (respuesta.isPresent()) {
            profesorRepositorio.deleteById(idProfesor);
        } else {
            throw new ExcepcionServicio("Delete: No se ha encontrado el profesor con id = " + idProfesor);
        }
        
    }
    
}
