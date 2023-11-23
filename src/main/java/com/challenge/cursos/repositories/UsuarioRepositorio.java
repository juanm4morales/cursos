package com.challenge.cursos.repositories;


import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UsuarioRepositorio<T, ID extends Serializable> extends CrudRepository<T, ID>{

    
}