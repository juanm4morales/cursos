package com.challenge.cursos.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.challenge.cursos.models.Curso;

@Repository
public interface CursoRepositorio extends CrudRepository<Curso, Long> {

    @Query(
        value="SELECT c FROM Curso c WHERE c.profesorCurso.id = :idProfesor",
        nativeQuery = false)
    List<Curso> findAllByProfesor(@Param("idProfesor") Long idProfesor);

    @Query(        
        value="SELECT c FROM Curso c WHERE c.fechaInicio > current_date",
        nativeQuery = false)
    List<Curso> findAllAvailable();

    @Query(
        value="SELECT c FROM Curso c WHERE EXISTS (SELECT ic FROM InscripcionCurso ic WHERE ic.curso = c AND ic.alumno.id = :alumnoId)",
        nativeQuery = false)
    List<Curso> findAllInscribedForAlumno(@Param("alumnoId") Long alumnoId);

    @Query(
        value="SELECT c FROM Curso c WHERE c.fechaInicio > current_date AND NOT EXISTS (SELECT ic FROM InscripcionCurso ic WHERE ic.curso = c AND ic.alumno.id = :alumnoId)",
        nativeQuery = false)
    List<Curso> findAllAvailableNotInscribedForAlumno(@Param("alumnoId") Long alumnoId);
    
}
