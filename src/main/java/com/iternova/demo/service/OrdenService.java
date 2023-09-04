package com.iternova.demo.service;

import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface OrdenService {
    List<Orden> findAll();

    public Orden save(Orden orden);

    public String generarNumOrden();

    public List<Orden> findByUsuario(Usuario usuario);

    Optional<Orden> findById(Integer id);
}
