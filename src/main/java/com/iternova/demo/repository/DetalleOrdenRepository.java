package com.iternova.demo.repository;

import com.iternova.demo.model.DetalleOrden;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleOrdenRepository extends JpaRepository<DetalleOrden, Integer> {
}
