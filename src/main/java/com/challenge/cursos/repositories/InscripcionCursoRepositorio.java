package com.challenge.cursos.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.challenge.cursos.models.InscripcionCurso;

@Repository
public interface InscripcionCursoRepositorio extends CrudRepository<InscripcionCurso, Long> {
    @Query(
        value="SELECT i FROM InscripcionCurso i WHERE i.alumno.id = :idAlumno",
        nativeQuery = false)
    List<InscripcionCurso> findAllByAlumno(@Param("idAlumno") Long idAlumno);

    @Query(
        value="SELECT i FROM InscripcionCurso i WHERE i.curso.id = :idCurso",
        nativeQuery = false)
    List<InscripcionCurso> findAllByCurso(@Param("idCurso") Long idCurso);

    @Query(
        value="SELECT i FROM InscripcionCurso i WHERE i.curso.id = :idCurso AND i.alumno.id = :idAlumno",
        nativeQuery = false)
    Optional<InscripcionCurso> findByCursoAlumno(@Param("idCurso") Long idCurso, @Param("idAlumno") Long idAlumno);
    
    
}
