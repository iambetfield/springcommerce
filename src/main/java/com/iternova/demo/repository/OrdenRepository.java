package com.iternova.demo.repository;

import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenRepository extends JpaRepository<Orden, Integer> {

    List<Orden> findByUsuario(Usuario usuario);
}
