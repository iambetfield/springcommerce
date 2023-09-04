package com.iternova.demo.service;

import com.iternova.demo.model.Orden;
import com.iternova.demo.model.Usuario;
import com.iternova.demo.repository.OrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrdenServiceImp implements OrdenService{

    @Autowired
    private OrdenRepository ordenRepository;

    @Override
    public List<Orden> findAll() {
        return ordenRepository.findAll();
    }

    @Override
    public Orden save(Orden orden) {
        return ordenRepository.save(orden);
    }

    public String generarNumOrden(){
        int num= 0;
        String numeroConcatenado ="";
        List<Orden> ordenes = findAll();
        List<Integer> numeros = new ArrayList<>();

        ordenes.stream().forEach(o -> numeros.add(Integer.parseInt(o.getNumero())));
        if(ordenes.isEmpty()){
            num = 1;
        }else {
            num=numeros.stream().max(Integer::compare).get();
            num++;
        }
        if(num<10){
            numeroConcatenado="000000000"+String.valueOf(num);
        } else if (num <100) {
            numeroConcatenado="00000000"+String.valueOf(num);
        } else if (num < 1000) {
            numeroConcatenado="0000000"+String.valueOf(num);
        }

        return numeroConcatenado;
    }

    @Override
    public List<Orden> findByUsuario(Usuario usuario) {
        return ordenRepository.findByUsuario(usuario);
    }

    @Override
    public Optional<Orden> findById(Integer id) {
        return ordenRepository.findById(id);
    }
}
