package com.iternova.demo.service;

import com.iternova.demo.model.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UsuarioService {

    Optional<Usuario> findById(Integer id);

    Usuario save(Usuario usuario);

    Optional<Usuario> findByEmail(String email);

    List<Usuario> findAll();
}
