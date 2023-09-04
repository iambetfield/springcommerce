package com.iternova.demo.service;

import com.iternova.demo.model.DetalleOrden;
import com.iternova.demo.repository.DetalleOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DetalleOrdenServiceImp implements DetalleOrdenService {

    @Autowired
    private DetalleOrdenRepository detalleOrdenRepository;
    @Override
    public DetalleOrden save(DetalleOrden detalleOrden) {
        return detalleOrdenRepository.save(detalleOrden);
    }
}
