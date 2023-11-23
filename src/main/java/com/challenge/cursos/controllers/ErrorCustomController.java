package com.challenge.cursos.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorCustomController implements ErrorController{

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer code=Integer.valueOf(status.toString());
            switch (code) {
                case 400:
                    return "/errores/400.html";
                case 403:
                    return "/errores/403.html";
                case 404:
                    return "/errores/404.html";
                case 500:
                    return "/errores/500.html";
                case 503:
                    return "/errores/503.html";

                default:
                    return "/errores/error.html";              
            }
        }
        return "/errores/error.html";


    }
    
}
