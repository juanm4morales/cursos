package com.challenge.cursos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Deprecated
@Controller
@RequestMapping("/")
public class HomeController {
    @GetMapping("/")
    public String index(){
        return "index.html";
    }
    
}
