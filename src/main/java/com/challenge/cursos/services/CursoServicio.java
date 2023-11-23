package com.challenge.cursos.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.cursos.exceptions.ExcepcionServicio;
import com.challenge.cursos.models.Curso;
import com.challenge.cursos.models.Profesor;
import com.challenge.cursos.models.Turno;
import com.challenge.cursos.repositories.CursoRepositorio;
import com.challenge.cursos.repositories.ProfesorRepositorio;

import jakarta.transaction.Transactional;

@Service
public class CursoServicio {

    @Autowired
    private CursoRepositorio cursoRepositorio;

    @Autowired
    private ProfesorRepositorio profesorRepositorio;

    @Transactional
    public void crearCurso(String nombre, String descripcion, Long profesorId, Turno turno, Date fechaInicio, Date fechaFin){
        Curso curso = new Curso();
        Optional<Profesor> respuestaProfesor = profesorRepositorio.findById(profesorId);
        Profesor profesor = new Profesor();
        if (respuestaProfesor.isPresent()) {
            profesor = respuestaProfesor.get();
        }
        curso.setDescripcion(descripcion);
        curso.setNombre(nombre);
        curso.setProfesorCurso(profesor);
        curso.setTurno(turno);
        curso.setFechaInicio(fechaInicio);
        curso.setFechaFin(fechaFin);
        curso.setFechaAlta(new Date());
        cursoRepositorio.save(curso);
    }

    @Transactional
    public void crearCurso(Curso curso) throws ExcepcionServicio {
        try {
            curso.setFechaAlta(new Date());
            cursoRepositorio.save(curso);
        } catch (Exception e) {
            throw new ExcepcionServicio(e.getMessage());
        }
    }

    public List<Curso> listarCursos() {
        List<Curso> cursos = new ArrayList<>();
        cursos = (List<Curso>) cursoRepositorio.findAll();
        return cursos;
    }
    
    public List<Curso> listarCursosProfesor(Long idProfesor) {
        List<Curso> cursos = new ArrayList<>();
        cursos = (List<Curso>) cursoRepositorio.findAllByProfesor(idProfesor);
        return cursos;
    }

    public List<Curso> listarCursosDisponibles() {
        List<Curso> cursos = new ArrayList<>();
        cursos = (List<Curso>) cursoRepositorio.findAllAvailable();
        return cursos;
    }

    public List<Curso> listarCursosAlumnoInscripto(Long idAlumno) {
        List<Curso> cursos = new ArrayList<>();
        cursos = (List<Curso>) cursoRepositorio.findAllInscribedForAlumno(idAlumno);
        return cursos;
    }

    public List<Curso> listarCursosAlumnoNoInscripto(Long idAlumno) {
        List<Curso> cursos = new ArrayList<>();
        cursos = (List<Curso>) cursoRepositorio.findAllAvailableNotInscribedForAlumno(idAlumno);
        return cursos;
    }

    
    
    public Curso buscarCurso(Long idCurso) throws ExcepcionServicio {
        Optional<Curso> respuesta = cursoRepositorio.findById(idCurso);
        Curso curso = new Curso();
        if (respuesta.isPresent()) {
            curso = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se ha encontrado el curso con id = " + idCurso);
        }
        return curso;
    }

    @Transactional
    public void modificarCurso(Long cursoId, String nombre, String descripcion, Long profesorId, Turno turno, Date fechaInicio, Date fechaFin) {
        Optional<Curso> respuesta = cursoRepositorio.findById(cursoId);
        Optional<Profesor> respuestaProfesor = profesorRepositorio.findById(profesorId);
        Profesor profesor = new Profesor();
        if (respuestaProfesor.isPresent()) {
            profesor = respuestaProfesor.get();
        }
        if (respuesta.isPresent()) {
            Curso curso = respuesta.get();
            curso.setNombre(nombre);
            curso.setDescripcion(descripcion);
            curso.setProfesorCurso(profesor);
            curso.setTurno(turno);
            curso.setFechaInicio(fechaInicio);
            curso.setFechaFin(fechaFin);
            cursoRepositorio.save(curso);
        }
    }

    @Transactional
    public void eliminarCurso(Long idCurso) throws ExcepcionServicio {
        Optional<Curso> respuesta = cursoRepositorio.findById(idCurso);

        if (respuesta.isPresent()) {
            cursoRepositorio.deleteById(idCurso);
        } else {
            throw new ExcepcionServicio("Delete: No se ha encontrado el curso con id = " + idCurso);
        }
    }
}
