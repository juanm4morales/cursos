package com.challenge.cursos.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.challenge.cursos.exceptions.ExcepcionServicio;
import com.challenge.cursos.models.Alumno;
import com.challenge.cursos.models.Curso;
import com.challenge.cursos.models.InscripcionCurso;
import com.challenge.cursos.models.Profesor;
import com.challenge.cursos.services.AlumnoServicio;
import com.challenge.cursos.services.CursoServicio;
import com.challenge.cursos.services.InscripcionCursoServicio;
import com.challenge.cursos.services.ProfesorServicio;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/alumno")
public class AlumnoController {
    public static final String REGISTRAR = "Registrar";
    public static final String MODIFICAR = "Modificar";

    private Alumno alumno;

    private static final String template = "alumnoTemplate";

    @Autowired
    private AlumnoServicio alumnoServicio;

    @Autowired
    private CursoServicio cursoServicio;

    @Autowired
    private InscripcionCursoServicio inscripcionServicio;

    private void alumnoAttributes(Model model) {
        List<Alumno> listaAlumnos = alumnoServicio.listarAlumnos();
        model.addAttribute("alumnos", listaAlumnos);
        if (alumno!=null) {
            model.addAttribute("idAlumno", alumno.getId());
        }
        
    }

    private void formAttributes(Object entidad, String nombreEntidad, Model model, String titulo, String accion) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidad);
        model.addAttribute("accion", accion);
        model.addAttribute("rol", "/alumno/");
        model.addAttribute("template", template);
    }

    private <T> void listAttributes(List<T> entidades, String nombreEntidad, Model model, String titulo) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidades);
        model.addAttribute("rol", "/alumno/");
        model.addAttribute("template", template);
    }

    @GetMapping("/")
    public String alumno(Model model) {
    
        alumnoAttributes(model);
        return "alumno.html";
    }

    // PERFIL

    @PostMapping("/setAlumno")
    public String setAlumno(@RequestParam(required = false) Long idAlumno, RedirectAttributes attributes, Model model){

        try {
            this.alumno = alumnoServicio.buscarAlumno(idAlumno);
            attributes.addFlashAttribute("info", "Alumno seleccionado con éxito.");
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/alumno/";
        
    }


    @GetMapping("/perfil")
    public String perfil(Model model, RedirectAttributes attributes){
        try {
            alumnoServicio.buscarAlumno(alumno.getId());
            alumnoAttributes(model);
            formAttributes(alumno, "alumno", model, "Actualizar Perfil Alumno", "Actualizar");
            return "alumnoForm.html";

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", e.getMessage());
            System.out.println(e.getMessage());
            
        } catch (NullPointerException e) {
            attributes.addFlashAttribute("warning", "No ha seleccionado un alumno");
        }
        return "redirect:/alumno/";
    }

    @PostMapping("/perfil/guardar")
    public String actualizarPerfil(@Valid @ModelAttribute Alumno alumno, BindingResult result, Model model,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {
            formAttributes(alumno, "alumno", model, "Actualizar perfil", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "alumnoForm.html";
        }
        try {
            alumnoServicio.crearAlumno(alumno);
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        attributes.addFlashAttribute("success", "Perfil actualizado con éxito.");
        return "redirect:/alumno/";
    }

    // CURSOS

  

    @GetMapping("/cursos")
    public String listarCursosFiltrados(@RequestParam(name = "filtro", required = false, defaultValue = "disponibles") String filtro, Model model, RedirectAttributes attributes){
        alumnoAttributes(model);
        List<Curso> listaCursos = new ArrayList<Curso>();

        if(this.alumno!=null) {
            
            if (filtro.equals("disponibles")) {
                listaCursos = cursoServicio.listarCursosAlumnoNoInscripto(this.alumno.getId());
                model.addAttribute("alta", false);
            } else if (filtro.equals("inscriptos")) {

                listaCursos = cursoServicio.listarCursosAlumnoInscripto(this.alumno.getId());
                model.addAttribute("alta", true);
            }
            
        }
        else {
            attributes.addFlashAttribute("warning", "No se ha seleccionado un alumno.");
        }
        
        listAttributes(listaCursos, "cursos", model, "Cursos disponibles");       
        return "cursoListAlumno.html";
    }



    @GetMapping("/cursos/inscripcion/{id}")
    public String inscripcion(@PathVariable("id") Long idCurso, Model model, RedirectAttributes attributes) {
        alumnoAttributes(model);
        if (this.alumno!=null) {
            inscripcionServicio.crearInscripcion(this.alumno.getId(), idCurso);

        }
        
        attributes.addFlashAttribute("success", "Se ha inscripto correctamente al curso.");
        return "redirect:/alumno/cursos";
    }

    @GetMapping("/cursos/baja/{id}")
    public String baja(@PathVariable("id") Long idCurso, Model model, RedirectAttributes attributes) {
        alumnoAttributes(model);
        if (this.alumno!=null) {
            try {
                inscripcionServicio.eliminarInscripcion(idCurso, this.alumno.getId());
            } catch (ExcepcionServicio e) {
                attributes.addFlashAttribute("error", e.getMessage());
            }

        }
        
        attributes.addFlashAttribute("success", "Se ha dado de baja correctamente al curso.");
        return "redirect:/alumno/cursos";
    }

    
}
