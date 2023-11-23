package com.challenge.cursos.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.challenge.cursos.exceptions.ExcepcionServicio;
import com.challenge.cursos.models.Alumno;
import com.challenge.cursos.models.Curso;
import com.challenge.cursos.models.InscripcionCurso;
import com.challenge.cursos.repositories.AlumnoRepositorio;
import com.challenge.cursos.repositories.CursoRepositorio;
import com.challenge.cursos.repositories.InscripcionCursoRepositorio;

import jakarta.transaction.Transactional;

@Service
public class InscripcionCursoServicio {

    @Autowired
    private InscripcionCursoRepositorio inscripcionRepositorio;

    @Autowired
    private CursoRepositorio cursoRepositorio;

    @Autowired
    private AlumnoRepositorio alumnoRepositorio;

    @Transactional
    public void crearInscripcion(Long idAlumno, Long idCurso) {
        InscripcionCurso inscripcion = new InscripcionCurso();
        Optional<Alumno> respuestaAlumno = alumnoRepositorio.findById(idAlumno);
        Optional<Curso> respuestaCurso = cursoRepositorio.findById(idCurso);

        Alumno alumno = new Alumno();
        Curso curso = new Curso();
        if (respuestaAlumno.isPresent() && respuestaCurso.isPresent()) {
            alumno = respuestaAlumno.get();
            curso = respuestaCurso.get();
        }

        inscripcion.setAlumno(alumno);
        inscripcion.setCurso(curso);
        inscripcion.setFechaInscripcion(new Date());

        inscripcionRepositorio.save(inscripcion);
    }

    @Transactional
    public void crearInscripcion(InscripcionCurso inscripcion) throws ExcepcionServicio {
        try {
            inscripcion.setFechaInscripcion(new Date());
            inscripcionRepositorio.save(inscripcion);
        } catch (Exception e) {
            throw new ExcepcionServicio(e.getMessage());
        }
    }

    

    public List<InscripcionCurso> listarInscripciones() {
        List<InscripcionCurso> inscripciones = new ArrayList<>();
        inscripciones = (List<InscripcionCurso>) inscripcionRepositorio.findAll();
        return inscripciones;
    }
    
    public List<InscripcionCurso> listarInscripcionesAlumno(Long idProfesor) {
        List<InscripcionCurso> inscripciones = new ArrayList<>();
        inscripciones = (List<InscripcionCurso>) inscripcionRepositorio.findAllByAlumno(idProfesor);
        return inscripciones;
    }

    public List<InscripcionCurso> listarInscripcionesCurso(Long idCurso) {
        List<InscripcionCurso> inscripciones = new ArrayList<>();
        inscripciones = (List<InscripcionCurso>) inscripcionRepositorio.findAllByCurso(idCurso);
        return inscripciones;
    }
    
    public InscripcionCurso buscarInscripcion(Long idInscripcion) throws ExcepcionServicio {
        Optional<InscripcionCurso> respuesta = inscripcionRepositorio.findById(idInscripcion);
        InscripcionCurso inscripcion = new InscripcionCurso();
        if (respuesta.isPresent()) {
            inscripcion = respuesta.get();
        } else {
            throw new ExcepcionServicio("No se ha encontrado el curso con id = " + idInscripcion);
        }
        return inscripcion;
    }

    

    @Transactional
    public void eliminarInscripcion(Long idInscripcion) throws ExcepcionServicio {
        Optional<InscripcionCurso> respuesta = inscripcionRepositorio.findById(idInscripcion);

        if (respuesta.isPresent()) {
            inscripcionRepositorio.deleteById(idInscripcion);
        } else {
            throw new ExcepcionServicio("Delete: No se ha encontrado la inscripción al cursado con id = " + idInscripcion);
        }
    }

    @Transactional
    public void eliminarInscripcion(Long idAlumno, Long idCurso) throws ExcepcionServicio {
        Optional<InscripcionCurso> respuesta = inscripcionRepositorio.findByCursoAlumno(idAlumno, idCurso);
        

        if (respuesta.isPresent()) {
            inscripcionRepositorio.delete(respuesta.get());;

        } else {
            throw new ExcepcionServicio("Delete: No se ha encontrado la inscripción correspondiente.");
        }
    }

    
}
