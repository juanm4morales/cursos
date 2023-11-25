package com.challenge.cursos.services;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.challenge.cursos.exceptions.ExcepcionServicio;
import com.challenge.cursos.models.Alumno;

import com.challenge.cursos.repositories.AlumnoRepositorio;

import jakarta.transaction.Transactional;

@Service
public class AlumnoServicio {

    @Autowired
    AlumnoRepositorio alumnoRepositorio;

    @Transactional
    public void crearAlumno(String nombre, String apellido, String email) {

        Alumno alumno = new Alumno();
        alumno.setNombre(nombre);
        alumno.setNombre(apellido);
        alumno.setEmail(email);


        alumnoRepositorio.save(alumno);

    }

    @Transactional
    public void crearAlumno(Alumno alumno) throws ExcepcionServicio {
        try {
            
            alumnoRepositorio.save(alumno);
            


        } catch (Exception e) {
            
            throw new ExcepcionServicio(e.getMessage());
        }
    }

    public List<Alumno> listarAlumnos() {
        return (List<Alumno>) alumnoRepositorio.findAll();
    }

    public Alumno buscarAlumno(Long idAlumno) throws ExcepcionServicio {
        Optional<Alumno> respuesta = alumnoRepositorio.findById(idAlumno);
        Alumno alumno = new Alumno();
        if (respuesta.isPresent()) {
            alumno = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se ha encontrado el alumno con id = " + idAlumno);
        }
        return alumno;
    }

    @Transactional
    public void modificarAlumno(Long idAlumno, String nombre, String apellido, String email, Integer legajo) {
        Optional<Alumno> respuesta = alumnoRepositorio.findById(idAlumno);
        if (respuesta.isPresent()) {
            Alumno alumno = respuesta.get();

            alumno.setNombre(nombre);
            alumno.setApellido(apellido);
            alumno.setEmail(email);
            alumno.setLegajo(legajo);
            alumnoRepositorio.save(alumno);
        }
    }

    @Transactional
    public void eliminarAlumno(Long idAlumno) throws ExcepcionServicio {
        Optional<Alumno> respuesta = alumnoRepositorio.findById(idAlumno);

        if (respuesta.isPresent()) {
            alumnoRepositorio.deleteById(idAlumno);
        } else {
            throw new ExcepcionServicio("Delete: No se ha encontrado el alumno con id = " + idAlumno);
        }
    }
    
}
