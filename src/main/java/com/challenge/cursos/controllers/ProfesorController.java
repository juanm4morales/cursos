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
import com.challenge.cursos.models.Curso;
import com.challenge.cursos.models.InscripcionCurso;
import com.challenge.cursos.models.Profesor;
import com.challenge.cursos.services.CursoServicio;
import com.challenge.cursos.services.InscripcionCursoServicio;
import com.challenge.cursos.services.ProfesorServicio;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/profesor")
public class ProfesorController {
    public static final String REGISTRAR = "Registrar";
    public static final String MODIFICAR = "Modificar";

    private Profesor profesor;

    private static final String template = "profesorTemplate";

    @Autowired
    private ProfesorServicio profesorServicio;

    @Autowired
    private InscripcionCursoServicio inscripcionServicio;

    @Autowired
    private CursoServicio cursoServicio;

    private void profesorAttributes(Model model) {
        List<Profesor> listaProfesores = profesorServicio.listarProfesores();
        model.addAttribute("profesores", listaProfesores);
        if (profesor!=null) {
            model.addAttribute("idProfesor", profesor.getId());
        }
        
    }

    private void formAttributes(Object entidad, String nombreEntidad, Model model, String titulo, String accion) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidad);
        model.addAttribute("accion", accion);
        model.addAttribute("rol", "/profesor/");
        model.addAttribute("template", template);
    }

    private <T> void listAttributes(List<T> entidades, String nombreEntidad, Model model, String titulo) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidades);
        model.addAttribute("rol", "/profesor/");
        model.addAttribute("template", template);
    }

    @GetMapping("/")
    public String profesor(Model model) {
    
        profesorAttributes(model);
        return "profesor.html";
    }

    // PERFIL

    @PostMapping("/setProfesor")
    public String setProfesor(@RequestParam(required = false) Long idProfesor, RedirectAttributes attributes, Model model){

        try {
            this.profesor = profesorServicio.buscarProfesor(idProfesor);
            attributes.addFlashAttribute("info", "Profesor seleccionado con éxito.");
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/profesor/";
        

        
    }


    @GetMapping("/perfil")
    public String perfil(Model model, RedirectAttributes attributes){
        try {
            profesorServicio.buscarProfesor(profesor.getId());
            profesorAttributes(model);
            formAttributes(profesor, "profesor", model, "Actualizar Perfil Profesor", "Actualizar");
            return "profesorForm.html";

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", e.getMessage());
            System.out.println(e.getMessage());
            
        } catch (NullPointerException e) {
            attributes.addFlashAttribute("warning", "No ha seleccionado un profesor");
        }
        return "redirect:/profesor/";
    }

    @PostMapping("/perfil/guardar")
    public String actualizarPerfil(@Valid @ModelAttribute Profesor profesor, BindingResult result, Model model,
            RedirectAttributes attributes) {
        if (result.hasErrors()) {

            formAttributes(profesor, "profesor", model, "Registrar/Modificar profesor", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "profesorForm.html";
        }
        profesorServicio.crearProfesor(profesor);
        attributes.addFlashAttribute("success", "Profesor registrado con éxito.");
        return "redirect:/profesor/perfil";
    }

    // CURSOS

    @GetMapping("/cursos/crear")
    public String crearCurso(Model model, RedirectAttributes attributes){
        profesorAttributes(model);
        Curso curso = new Curso();
        try {
            profesorServicio.buscarProfesor(profesor.getId());
            formAttributes(curso, "curso", model, "Registrar Curso", REGISTRAR);
            model.addAttribute("profesorCurso", profesor);
            return "cursoFormProfesor.html";
        } catch (ExcepcionServicio e) {
            if (profesor.getId()==0) {
                attributes.addFlashAttribute("warning", "Atención: No se ha seleccionado profesor.");
            } else {
                attributes.addFlashAttribute("error", "Error: No se ha encontrado al profesor.");
            }
            System.out.println(e.getMessage());
            return "redirect:/profesor/";
        }
    }
    
    @GetMapping("/cursos/modificar/{id}")
    public String modificarCurso(@PathVariable("id") Long idCurso, Model model, RedirectAttributes attributes){
        try {
            Curso curso = cursoServicio.buscarCurso(idCurso);
            

            formAttributes(curso, "curso", model, "Modificar Curso", MODIFICAR);
            model.addAttribute("profesorCurso", profesor);

            
            return "cursoFormProfesor.html";

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del curso no existe.");
            System.out.println(e.getMessage());
            return "redirect:/admin/cursos";
        }
    }

    @GetMapping("/cursos/eliminar/{id}")
    public String eliminarCurso(@PathVariable("id") Long idCurso, RedirectAttributes attributes) {

        try {
            cursoServicio.eliminarCurso(idCurso);
            attributes.addFlashAttribute("success", "Curso eliminado correctamente.");
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del curso no existe.");
            System.out.println(e.getMessage());
        }
        return "redirect:/profesor/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model, RedirectAttributes attributes){
        profesorAttributes(model);
        List<Curso> listaCursos = new ArrayList<Curso>();
        if (profesor!=null) {
            listaCursos = cursoServicio.listarCursosProfesor(profesor.getId());
        }
        else {
            attributes.addFlashAttribute("warning", "No se ha seleccionado profesor.");
        }
        
        listAttributes(listaCursos, "cursos", model, "Cursos creados");        
        return "cursoList.html";
    }

    @PostMapping("/cursos/guardar")
    public String guardarCurso(@Valid @ModelAttribute Curso curso, BindingResult result, Model model, RedirectAttributes attributes) throws ExcepcionServicio{
        

        if (result.hasErrors()) {
            
            formAttributes(curso, "curso", model, "Registrar/Modificar Curso", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "profesorForm.html";
        }
        cursoServicio.crearCurso(curso);
        attributes.addFlashAttribute("success", "Curso registrado con éxito.");
        return "redirect:/profesor/cursos";
    }

    @GetMapping("/cursos/inscripciones/{id}")
    public String listarInscripciones(@PathVariable("id") Long idCurso, Model model,  RedirectAttributes attributes){
        profesorAttributes(model);
        try {
            Curso curso = cursoServicio.buscarCurso(idCurso);
            if (curso.getProfesorCurso().getId()!=this.profesor.getId()){
                attributes.addFlashAttribute("error", "El curso seleccionado no corresponde al profesor.");
            }
            else {
                List<InscripcionCurso> listaInscripciones = inscripcionServicio.listarInscripcionesCurso(idCurso);
                listAttributes(listaInscripciones, "inscripciones", model, "Inscripciones - "+curso.getNombre());
                return "inscripcionList.html";
            }
            
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error",e.getMessage());
            
        }
        return "redirect:/profesor/cursos";
    }    
}
