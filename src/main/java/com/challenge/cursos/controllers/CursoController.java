package com.challenge.cursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/curso")
public class CursoController {

    @GetMapping("/crear")
    public String crear(){
        return "cursoForm.html";
    }
    
}
