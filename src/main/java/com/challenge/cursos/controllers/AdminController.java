package com.challenge.cursos.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

/**
 * Controlador asignado al perfil de administrador. Posee control total sobre las entidades: Alumno, Profesor, Curso. 
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    public static final String REGISTRAR = "Registrar";
    public static final String MODIFICAR = "Modificar";

    private static final String template = "adminTemplate";

    @Autowired
    private ProfesorServicio profesorServicio;

    @Autowired
    private AlumnoServicio alumnoServicio;

    @Autowired
    private CursoServicio cursoServicio;

    @Autowired
    private InscripcionCursoServicio inscripcionServicio;

    private <T> void listAttributes(List<T> entidades, String nombreEntidad, Model model, String titulo) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidades);
        model.addAttribute("rol", "/admin/");
        model.addAttribute("template",template);
    }

    private void formAttributes(Object entidad, String nombreEntidad, Model model, String titulo, String accion) {
        model.addAttribute("titulo", titulo);
        model.addAttribute(nombreEntidad, entidad);
        model.addAttribute("accion", accion);
        model.addAttribute("rol", "/admin/");
        model.addAttribute("template",template);
    }

    private <T> void formListAttributes(Object entidad, String nombreEntidad, Model model, String titulo, String accion, List<T> lista, String nombreLista) {
        formAttributes(entidad, nombreEntidad, model, titulo, accion);
        model.addAttribute(nombreLista, lista);
    }

    @GetMapping("/")
    public String admin() {
        return "admin.html";
    }

    // PROFESORES
    @GetMapping("/profesores/crear")
    public String crearProfesor(Model model){
        Profesor profesor = new Profesor();
        formAttributes(profesor, "profesor", model, "Registrar profesor", REGISTRAR);
        return "profesorForm.html";
    }

    @GetMapping("/profesores")
    public String listarProfesores(Model model){
        List<Profesor> listaProfesores = profesorServicio.listarProfesores();
        listAttributes(listaProfesores, "profesores", model, "Lista de profesores");
        
        return "profesorList.html";
    }

    @GetMapping("/profesores/modificar/{id}")
    public String modificarProfesor(@PathVariable("id") Long idProfesor, Model model, RedirectAttributes attributes){
        try {
            Profesor profesor = profesorServicio.buscarProfesor(idProfesor);

            formAttributes(profesor, "profesor", model, "Modificar profesor", MODIFICAR);
            return "profesorForm.html";

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del profesor no existe.");
            System.out.println(e.getMessage());
            return "redirect:/admin/profesores";
        }
    }

    @GetMapping("/profesores/eliminar/{id}")
    public String eliminarProfesor(@PathVariable("id") Long idProfesor, RedirectAttributes attributes) {

        try {
            profesorServicio.eliminarProfesor(idProfesor);
            attributes.addFlashAttribute("success", "Profesor eliminado correctamente.");
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del profesor no existe.");
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/profesores";
    }

    

    @PostMapping("/profesores/guardar")
    public String guardarProfesor(@Valid @ModelAttribute Profesor profesor, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            
            formAttributes(profesor, "profesor", model, "Registrar/Modificar profesor", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "profesorForm.html";
        }
        profesorServicio.crearProfesor(profesor);
        attributes.addFlashAttribute("success", "Profesor registrado con éxito.");
        return "redirect:/admin/profesores";
    }

    // ALUMNOS 

    @GetMapping("/alumnos/crear")
    public String crearAlumno(Model model){
        Alumno alumno = new Alumno();
        formAttributes(alumno, "alumno", model, "Registrar Alumno", REGISTRAR);
        return "alumnoForm.html";
    }
    
    @GetMapping("/alumnos/modificar/{id}")
    public String modificarAlumno(@PathVariable("id") Long idAlumno, Model model, RedirectAttributes attributes){
        try {
            Alumno alumno = alumnoServicio.buscarAlumno(idAlumno);
            formAttributes(alumno, "alumno", model, "Modificar Alumno", MODIFICAR);

            return "alumnoForm.html";

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del alumno no existe.");
            System.out.println(e.getMessage());
            return "redirect:/admin/alumnos";
        }
    }

    @GetMapping("/alumnos/eliminar/{id}")
    public String eliminarAlumno(@PathVariable("id") Long idAlumno, RedirectAttributes attributes) {

        try {
            alumnoServicio.eliminarAlumno(idAlumno);
            attributes.addFlashAttribute("success", "Alumno eliminado correctamente.");
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("warning", "Atención: El ID del alumno no existe.");
            System.out.println(e.getMessage());
        }
        return "redirect:/admin/alumnos";
    }

    @GetMapping("/alumnos")
    public String listarAlumnos(Model model){
        
        List<Alumno> listaAlumnos = alumnoServicio.listarAlumnos();
        listAttributes(listaAlumnos, "alumnos", model, "Lista de alumnos");
        
        return "alumnoList.html";
    }

    @PostMapping("/alumnos/guardar")
    public String guardarAlumno(@Valid @ModelAttribute Alumno alumno, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            formAttributes(alumno, "alumno", model, "Registrar/Modificar profesor", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "alumnoForm.html";
        }
        try {
            alumnoServicio.crearAlumno(alumno);
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        attributes.addFlashAttribute("success", "Alumno registrado con éxito.");
        return "redirect:/admin/alumnos";
    }

    // CURSOS

    @GetMapping("/cursos/crear")
    public String crearCurso(Model model){
        Curso curso = new Curso();
        List<Profesor> listaProfesores = profesorServicio.listarProfesores();
        
        formListAttributes(curso, "curso", model, "Registrar curso", REGISTRAR, listaProfesores, "profesores");
        return "cursoForm.html";
    }
    
    @GetMapping("/cursos/modificar/{id}")
    public String modificarCurso(@PathVariable("id") Long idCurso, Model model, RedirectAttributes attributes){
        try {
            Curso curso = cursoServicio.buscarCurso(idCurso);
            List<Profesor> listaProfesores = profesorServicio.listarProfesores();
            formListAttributes(curso, "curso", model, "Modificar curso", MODIFICAR, listaProfesores, "profesores");
            
            return "cursoForm.html";

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
        return "redirect:/admin/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model){
        
        List<Curso> listaCursos = cursoServicio.listarCursos();
        listAttributes(listaCursos, "cursos", model, "Lista de cursos");
        
        return "cursoList.html";
    }

    @PostMapping("/cursos/guardar")
    public String guardarCurso(@Valid @ModelAttribute Curso curso, BindingResult result, Model model, RedirectAttributes attributes){
        if (result.hasErrors()){
            
            formAttributes(curso, "curso", model, "Registrar/Modificar curso", "Actualizar");

            System.out.println("Hubo errores en el formulario");
            return "cursoForm.html";
        }
        try {
            cursoServicio.crearCurso(curso);
        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());
        }
        attributes.addFlashAttribute("success", "Curso registrado con éxito.");

        return "redirect:/admin/cursos";
    }

    // Inscripciones

    @GetMapping("/inscripciones")
    public String listarInscripciones(Model model){

        List<InscripcionCurso> listaInscripciones = inscripcionServicio.listarInscripciones();
        listAttributes(listaInscripciones, "inscripciones", model, "Lista de inscripciones");
        
        return "inscripcionList.html";
    }

    @GetMapping("/cursos/inscripciones/{id}")
    public String listarInscripciones(@PathVariable("id") Long idCurso, Model model, RedirectAttributes attributes) {
        
        try {
            Curso curso = cursoServicio.buscarCurso(idCurso);
            
            List<InscripcionCurso> listaInscripciones = inscripcionServicio.listarInscripcionesCurso(idCurso);
            listAttributes(listaInscripciones, "inscripciones", model,
                    "Inscripciones - " + curso.getNombre());
            return "inscripcionList.html";

            

        } catch (ExcepcionServicio e) {
            attributes.addFlashAttribute("error", e.getMessage());

        }
        return "redirect:/admin/cursos";

    }



    
}
