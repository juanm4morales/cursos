package com.challenge.cursos.validators;

import org.springframework.beans.factory.annotation.Autowired;

import com.challenge.cursos.repositories.AlumnoRepositorio;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidadorLegajoUnico implements ConstraintValidator<LegajoUnico, Integer> {

    @Autowired
    AlumnoRepositorio alumnoRepositorio;

    @Override
    public boolean isValid(Integer legajo, ConstraintValidatorContext context) {

        return !alumnoRepositorio.existsByLegajo(legajo);
    }
    
}
